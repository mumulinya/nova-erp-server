package cn.iocoder.yudao.module.erp.service.stock;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.stock.vo.aisuggest.ErpStockAiSuggestRespVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockAiSuggestDO;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.ErpStockAiSuggestMapper;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI 库存优化建议 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class ErpStockAiSuggestServiceImpl implements ErpStockAiSuggestService {

    private ChatClient chatClient;

    @Resource(name = "openAiChatModel")
    private org.springframework.ai.chat.model.ChatModel chatModel;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.chatClient = ChatClient.create(chatModel);
    }

    @Resource
    private ErpStockAiSuggestMapper aiSuggestMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ErpStockAiSuggestRespVO> generateAiSuggest() {
        // 1. 获取库存统计数据
        List<Map<String, Object>> stockStats = aiSuggestMapper.selectStockStatsForAi();
        if (CollUtil.isEmpty(stockStats)) {
            return Collections.emptyList();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(30);

        // 2. 获取近30天出库统计
        List<Map<String, Object>> outStats = aiSuggestMapper.selectStockOutStats(startTime, now);
        // key: productId + "_" + warehouseId
        Map<String, Integer> outCountMap = outStats.stream().collect(Collectors.toMap(
                map -> map.get("productId") + "_" + map.get("warehouse_id"),
                map -> new BigDecimal(map.get("totalOutCount").toString()).intValue(),
                (v1, v2) -> v1
        ));

        // 3. 组装 Prompt 数据并计算日均销量、库存占用率
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append("产品名称 | 仓库名称 | 当前库存 | 安全库存 | 最大库存 | 库存占用率 | 日均销量 | 预计可售天数\n");

        for (Map<String, Object> stat : stockStats) {
            Long productId = ((Number) stat.get("productId")).longValue();
            Long warehouseId = ((Number) stat.get("warehouseId")).longValue();
            String productName = (String) stat.get("productName");
            String warehouseName = (String) stat.get("warehouseName");
            BigDecimal currentStock = new BigDecimal(stat.get("currentStock").toString());
            BigDecimal safetyStock = new BigDecimal(stat.get("safetyStock").toString());
            BigDecimal maxStock = new BigDecimal(stat.get("maxStock").toString());

            int totalOut30Days = outCountMap.getOrDefault(productId + "_" + warehouseId, 0);
            BigDecimal avgDailySale = BigDecimal.valueOf(totalOut30Days).divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);

            String saleableDays = "999+";
            if (avgDailySale.compareTo(BigDecimal.ZERO) > 0) {
                saleableDays = currentStock.divide(avgDailySale, 0, RoundingMode.HALF_UP).toString();
            }

            // 计算库存占用率 = 当前库存 / 最大库存 × 100%
            String usageRate = "N/A";
            if (maxStock.compareTo(BigDecimal.ZERO) > 0) {
                usageRate = currentStock.divide(maxStock, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP) + "%";
            }

            dataBuilder.append(String.format("%s | %s | %s | %s | %s | %s | %s | %s\n",
                    productName, warehouseName, currentStock.toPlainString(), safetyStock.toPlainString(),
                    maxStock.toPlainString(), usageRate, avgDailySale.toPlainString(), saleableDays));

            // 把计算结果存回 stat Map, 方便后续保存
            stat.put("avgDailySale", avgDailySale);
            stat.put("currentStockBD", currentStock);
            stat.put("safetyStockBD", safetyStock);
            stat.put("maxStockBD", maxStock);
        }

        // 4. 构建完整的 Prompt
        String promptText = "你是一个仓库库存管理专家, 请根据以下库存数据给出优化建议。\n\n"
                + "【库存数据列表】\n" + dataBuilder.toString() + "\n\n"
                + "请对每个产品给出库存优化建议, 返回严格的JSON数组,\n"
                + "不要返回任何其他文字, 只返回JSON:\n"
                + "[\n"
                + "  {\n"
                + "    \"productName\": \"产品名称\",\n"
                + "    \"warehouseName\": \"仓库名称\",\n"
                + "    \"suggestType\": 建议类型(1=补货 2=清仓 3=调拨 4=正常),\n"
                + "    \"suggestContent\": \"具体优化建议(100字以内, 需包含具体数值分析)\",\n"
                + "    \"priority\": 优先级(1=紧急 2=普通 3=低)\n"
                + "  }\n"
                + "]\n\n"
                + "判断规则(按优先级从高到低匹配):\n"
                + "1. 库存不足补货规则:\n"
                + "   - 当前库存 < 安全库存 且 预计可售天数 < 7天 -> 类型1补货, 优先级1紧急, 建议内容需说明缺口数量和紧急补货量\n"
                + "   - 当前库存 < 安全库存 -> 类型1补货, 优先级2普通, 建议内容需说明建议补货到最大库存的数量\n"
                + "2. 库存积压清仓规则(重点关注):\n"
                + "   - 库存占用率 >= 90% -> 类型2清仓, 优先级1紧急, 建议内容需说明积压金额和建议清仓比例\n"
                + "   - 库存占用率 >= 70% 且 日均销量 < 1 -> 类型2清仓, 优先级2普通, 建议打折促销\n"
                + "   - 库存占用率 >= 50% 且 预计可售天数 > 90天 -> 类型2清仓, 优先级2普通, 建议更换销售渠道\n"
                + "3. 调拨规则:\n"
                + "   - 当前库存处于安全库存和最大库存之间但分布不均时 -> 类型3调拨, 优先级2普通\n"
                + "4. 正常:\n"
                + "   - 安全库存 <= 当前库存 < 最大库存x0.5 且 日均销量正常 -> 类型4正常, 优先级3低\n\n"
                + "注意:\n"
                + "- 库存占用率是当前库存占最大库存的百分比, 超过90%说明仓库快满了\n"
                + "- 建议内容中必须包含具体数字(如缺口XX件、建议补货XX件、积压XX件等)\n"
                + "- 如果最大库存为0或未配置, 跳过库存积压判断, 仅按安全库存规则判断";

        // 5. 调用 AI
        String responseContent;
        try {
            responseContent = chatClient.prompt()
                    .user(promptText)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("调用AI大模型失败", e);
            throw ServiceExceptionUtil.exception0(0, "AI生成库存优化建议失败，请稍后重试");
        }

        // 6. 解析 JSON
        String jsonStr = extractJsonFromMarkdown(responseContent);
        JSONArray jsonArray;
        try {
            jsonArray = JSONUtil.parseArray(jsonStr);
        } catch (Exception e) {
            log.error("解析AI返回的JSON失败: {}", jsonStr, e);
            throw ServiceExceptionUtil.exception0(0, "AI返回格式错误，请重新生成");
        }

        // 7. 批量保存
        LocalDateTime nowTime = LocalDateTime.now();
        List<ErpStockAiSuggestDO> suggestDOList = jsonArray.stream().map(obj -> {
            AiStockSuggestJsonResult result = JSONUtil.toBean((cn.hutool.json.JSONObject) obj, AiStockSuggestJsonResult.class);
            // 匹配对应的原始数据
            Map<String, Object> originalStat = stockStats.stream()
                    .filter(s -> s.get("productName").equals(result.getProductName()) && s.get("warehouseName").equals(result.getWarehouseName()))
                    .findFirst()
                    .orElse(null);
            
            if (originalStat == null) return null;

            ErpStockAiSuggestDO suggestDO = new ErpStockAiSuggestDO();
            suggestDO.setProductId(((Number) originalStat.get("productId")).longValue());
            suggestDO.setProductName(result.getProductName());
            suggestDO.setWarehouseId(((Number) originalStat.get("warehouseId")).longValue());
            suggestDO.setWarehouseName(result.getWarehouseName());
            suggestDO.setCurrentStock(((BigDecimal) originalStat.get("currentStockBD")).intValue());
            suggestDO.setSafetyStock(((BigDecimal) originalStat.get("safetyStockBD")).intValue());
            suggestDO.setMaxStock(((BigDecimal) originalStat.get("maxStockBD")).intValue());
            suggestDO.setAvgDailySale((BigDecimal) originalStat.get("avgDailySale"));
            suggestDO.setSuggestType(result.getSuggestType());
            suggestDO.setSuggestContent(result.getSuggestContent());
            suggestDO.setPriority(result.getPriority());
            suggestDO.setStatus(0);
            return suggestDO;
        }).filter(item -> item != null).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(suggestDOList)) {
            aiSuggestMapper.insertBatch(suggestDOList);
        }

        return BeanUtils.toBean(suggestDOList, ErpStockAiSuggestRespVO.class);
    }

    @Override
    public List<ErpStockAiSuggestRespVO> getLatestAiSuggest() {
        List<ErpStockAiSuggestDO> list = aiSuggestMapper.selectLatestSuggests();
        return BeanUtils.toBean(list, ErpStockAiSuggestRespVO.class);
    }

    @Override
    public void handleAiSuggest(Long id) {
        aiSuggestMapper.updateById(ErpStockAiSuggestDO.builder()
                .id(id)
                .status(1)
                .build());
    }

    @Override
    public void ignoreAiSuggest(Long id) {
        aiSuggestMapper.updateById(ErpStockAiSuggestDO.builder()
                .id(id)
                .status(2)
                .build());
    }

    private String extractJsonFromMarkdown(String content) {
        if (StrUtil.isBlank(content)) {
            return "[]";
        }
        String json = content.trim();
        if (json.startsWith("```json")) {
            json = json.substring(7);
        } else if (json.startsWith("```")) {
            json = json.substring(3);
        }
        if (json.endsWith("```")) {
            json = json.substring(0, json.length() - 3);
        }
        return json.trim();
    }

    @Data
    public static class AiStockSuggestJsonResult {
        private String productName;
        private String warehouseName;
        private Integer suggestType;
        private String suggestContent;
        private Integer priority;
    }

}
