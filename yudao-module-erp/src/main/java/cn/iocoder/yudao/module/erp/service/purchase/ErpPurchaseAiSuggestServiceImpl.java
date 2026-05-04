package cn.iocoder.yudao.module.erp.service.purchase;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.aisuggest.ErpPurchaseAiSuggestRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseAiSuggestDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpSupplierDO;
import cn.iocoder.yudao.module.erp.dal.mysql.product.ErpProductMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseAiSuggestMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpSupplierMapper;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI采购建议 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class ErpPurchaseAiSuggestServiceImpl implements ErpPurchaseAiSuggestService {

    private ChatClient chatClient;

    @Resource(name = "openAiChatModel")
    private org.springframework.ai.chat.model.ChatModel chatModel;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.chatClient = ChatClient.create(chatModel);
    }

    @Resource
    private ErpPurchaseAiSuggestMapper aiSuggestMapper;

    @Resource
    private ErpSupplierMapper supplierMapper;

    @Resource
    private ErpProductMapper productMapper;

    @Resource
    private ErpPurchaseOrderService purchaseOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ErpPurchaseAiSuggestRespVO> generateAiSuggest() {
        // 1. 获取需要补货的产品列表（库存 < 安全库存）
        List<Map<String, Object>> needReplenishProducts = aiSuggestMapper.selectNeedReplenishProducts();
        if (CollUtil.isEmpty(needReplenishProducts)) {
            return Collections.emptyList();
        }

        List<Long> productIds = needReplenishProducts.stream()
                .map(map -> ((Number) map.get("productId")).longValue())
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(30);

        // 2. 获取近30天销量
        List<Map<String, Object>> saleCounts = aiSuggestMapper.selectProductSaleCounts(productIds, startTime, now);
        Map<Long, Integer> saleCountMap = saleCounts.stream().collect(Collectors.toMap(
                map -> ((Number) map.get("productId")).longValue(),
                map -> ((Number) map.get("totalSaleCount")).intValue(),
                (v1, v2) -> v1
        ));

        // 3. 获取近30天采购记录
        List<Map<String, Object>> purchaseRecords = aiSuggestMapper.selectProductRecentPurchaseRecords(productIds, startTime, now);
        Map<Long, String> purchaseSupplierMap = purchaseRecords.stream().collect(Collectors.toMap(
                map -> ((Number) map.get("productId")).longValue(),
                map -> (String) map.get("supplierName"),
                (v1, v2) -> v1 // 取第一条（最新的一条）
        ));

        // 4. 组装 Prompt
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("你是一个供应链采购专家，请根据以下库存和销售数据给出采购补货建议。\n\n");
        promptBuilder.append("【需要补货的产品列表】\n");
        promptBuilder.append("产品名称 | 当前库存 | 安全库存 | 近30天销售量 | 历史采购供应商\n");

        for (Map<String, Object> product : needReplenishProducts) {
            Long productId = ((Number) product.get("productId")).longValue();
            String productName = (String) product.get("productName");
            int currentStock = ((Number) product.get("currentStock")).intValue();
            int safetyStock = ((Number) product.get("safetyStock")).intValue();
            int saleCount = saleCountMap.getOrDefault(productId, 0);
            String supplierName = purchaseSupplierMap.getOrDefault(productId, "无历史记录");

            promptBuilder.append(String.format("%s | %d | %d | %d | %s\n",
                    productName, currentStock, safetyStock, saleCount, supplierName));
        }

        promptBuilder.append("\n请对每个产品给出补货建议，返回严格的JSON数组格式，不要返回任何其他文字，只返回JSON：\n");
        promptBuilder.append("[\n  {\n");
        promptBuilder.append("    \"productName\": \"产品名称\",\n");
        promptBuilder.append("    \"suggestCount\": 建议采购数量(整数),\n");
        promptBuilder.append("    \"supplierName\": \"推荐供应商名称\",\n");
        promptBuilder.append("    \"suggestTime\": \"建议采购日期(yyyy-MM-dd)\",\n");
        promptBuilder.append("    \"reason\": \"分析原因(50字以内)\"\n");
        promptBuilder.append("  }\n]\n");

        // 5. 调用大模型
        String responseContent;
        try {
            responseContent = chatClient.prompt()
                    .user(promptBuilder.toString())
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("调用AI大模型失败", e);
            throw ServiceExceptionUtil.exception0(0, "AI生成采购建议失败，请稍后重试");
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

        // 7. 组装入库数据
        LocalDateTime createTime = LocalDateTime.now();
        List<ErpPurchaseAiSuggestDO> suggestDOList = jsonArray.stream().map(obj -> {
            AiSuggestJsonResult result = JSONUtil.toBean((cn.hutool.json.JSONObject) obj, AiSuggestJsonResult.class);
            // 找到对应的 productId
            Map<String, Object> productMap = needReplenishProducts.stream()
                    .filter(p -> p.get("productName").equals(result.getProductName()))
                    .findFirst()
                    .orElse(null);
            
            if (productMap == null) {
                return null;
            }

            ErpPurchaseAiSuggestDO suggestDO = new ErpPurchaseAiSuggestDO();
            suggestDO.setProductId(((Number) productMap.get("productId")).longValue());
            suggestDO.setProductName(result.getProductName());
            suggestDO.setCurrentStock(((Number) productMap.get("currentStock")).intValue());
            suggestDO.setSafetyStock(((Number) productMap.get("safetyStock")).intValue());
            suggestDO.setSuggestCount(result.getSuggestCount());
            suggestDO.setSupplierName(result.getSupplierName());
            try {
                suggestDO.setSuggestTime(LocalDate.parse(result.getSuggestTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } catch (Exception e) {
                suggestDO.setSuggestTime(LocalDate.now());
            }
            suggestDO.setReason(result.getReason());
            suggestDO.setStatus(0); // 待确认
            suggestDO.setCreateTime(createTime);
            return suggestDO;
        }).filter(item -> item != null).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(suggestDOList)) {
            aiSuggestMapper.insertBatch(suggestDOList);
        }

        return BeanUtils.toBean(suggestDOList, ErpPurchaseAiSuggestRespVO.class);
    }

    @Override
    public List<ErpPurchaseAiSuggestRespVO> getLatestAiSuggest() {
        List<ErpPurchaseAiSuggestDO> list = aiSuggestMapper.selectLatestSuggests();
        return BeanUtils.toBean(list, ErpPurchaseAiSuggestRespVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmAiSuggest(Long id) {
        ErpPurchaseAiSuggestDO suggestDO = aiSuggestMapper.selectById(id);
        if (suggestDO == null) {
            throw ServiceExceptionUtil.exception0(0, "采购建议不存在");
        }
        if (suggestDO.getStatus() != 0) {
            throw ServiceExceptionUtil.exception0(0, "采购建议已处理");
        }

        // 查找供应商
        String supplierName = suggestDO.getSupplierName();
        ErpSupplierDO supplierDO = supplierMapper.selectOne(ErpSupplierDO::getName, supplierName);
        if (supplierDO == null) {
            throw ServiceExceptionUtil.exception0(0, "推荐供应商【" + supplierName + "】不存在，请先创建或修改建议");
        }

        // 查找产品
        ErpProductDO productDO = productMapper.selectById(suggestDO.getProductId());
        if (productDO == null) {
            throw ServiceExceptionUtil.exception0(0, "关联产品不存在");
        }

        // 构建订单
        ErpPurchaseOrderSaveReqVO orderReqVO = new ErpPurchaseOrderSaveReqVO();
        orderReqVO.setSupplierId(supplierDO.getId());
        orderReqVO.setOrderTime(LocalDateTime.now());
        orderReqVO.setDiscountPercent(new BigDecimal("100")); // 默认无折扣
        
        ErpPurchaseOrderSaveReqVO.Item item = new ErpPurchaseOrderSaveReqVO.Item();
        item.setProductId(productDO.getId());
        item.setProductUnitId(productDO.getUnitId());
        item.setProductPrice(productDO.getPurchasePrice() != null ? productDO.getPurchasePrice() : BigDecimal.ZERO);
        item.setCount(new BigDecimal(suggestDO.getSuggestCount()));
        item.setTaxPercent(BigDecimal.ZERO); // 默认0税率
        item.setRemark("AI智能建议生成");

        orderReqVO.setItems(Collections.singletonList(item));

        // 创建订单
        purchaseOrderService.createPurchaseOrder(orderReqVO);

        // 更新状态
        aiSuggestMapper.updateById(ErpPurchaseAiSuggestDO.builder()
                .id(id)
                .status(1)
                .build());
    }

    @Override
    public void ignoreAiSuggest(Long id) {
        ErpPurchaseAiSuggestDO suggestDO = aiSuggestMapper.selectById(id);
        if (suggestDO == null) {
            throw ServiceExceptionUtil.exception0(0, "采购建议不存在");
        }
        aiSuggestMapper.updateById(ErpPurchaseAiSuggestDO.builder()
                .id(id)
                .status(2)
                .build());
    }

    @Override
    public ErpPurchaseAiSuggestRespVO getAiSuggest(Long id) {
        ErpPurchaseAiSuggestDO suggestDO = aiSuggestMapper.selectById(id);
        if (suggestDO == null) {
            throw ServiceExceptionUtil.exception0(0, "采购建议不存在");
        }
        return BeanUtils.toBean(suggestDO, ErpPurchaseAiSuggestRespVO.class);
    }

    @Override
    public void deleteAiSuggest(Long id) {
        ErpPurchaseAiSuggestDO suggestDO = aiSuggestMapper.selectById(id);
        if (suggestDO == null) {
            throw ServiceExceptionUtil.exception0(0, "采购建议不存在");
        }
        aiSuggestMapper.deleteById(id);
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
    public static class AiSuggestJsonResult {
        private String productName;
        private Integer suggestCount;
        private String supplierName;
        private String suggestTime;
        private String reason;
    }

}
