package cn.nova.erp.module.erp.service.sale;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.nova.erp.module.erp.controller.admin.sale.vo.aianalysis.ErpSaleAiAnalysisRespVO;
import cn.nova.erp.module.erp.dal.dataobject.sale.ErpSaleAiAnalysisDO;
import cn.nova.erp.module.erp.dal.mysql.sale.ErpSaleAiAnalysisMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


/**
 * AI 销售趋势分析 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class ErpSaleAiAnalysisServiceImpl implements ErpSaleAiAnalysisService {

    private ChatClient chatClient;

    @Resource(name = "openAiChatModel")
    private org.springframework.ai.chat.model.ChatModel chatModel;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.chatClient = ChatClient.create(chatModel);
    }

    @Resource
    private ErpSaleAiAnalysisMapper aiAnalysisMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpSaleAiAnalysisRespVO generateAiAnalysis() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixMonthsAgo = now.minusMonths(6);
        LocalDateTime thirtyDaysAgo = now.minusDays(30);

        // 1. 查询近6个月每月销售总额和订单数
        List<Map<String, Object>> monthlyTrend = aiAnalysisMapper.selectMonthlyTrend(sixMonthsAgo, now);

        // 2. 查询近30天热销产品TOP3
        List<Map<String, Object>> topProducts = aiAnalysisMapper.selectTopProducts(thirtyDaysAgo, now);

        // 3. 查询近30天优质客户TOP3
        List<Map<String, Object>> topCustomers = aiAnalysisMapper.selectTopCustomers(thirtyDaysAgo, now);

        // 4. 组装 Prompt
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("你是一个销售数据分析专家，请根据以下销售数据进行趋势分析并给出建议。\n\n");

        // 月度趋势
        promptBuilder.append("【近6个月销售趋势】\n");
        promptBuilder.append("月份 | 销售总额 | 订单数量\n");
        for (Map<String, Object> row : monthlyTrend) {
            promptBuilder.append(String.format("%s | %s | %s\n",
                    row.get("month"), row.get("amount"), row.get("orderCount")));
        }

        // 热销产品
        promptBuilder.append("\n【近30天热销产品TOP3】\n");
        promptBuilder.append("产品名称 | 销售数量 | 销售金额\n");
        for (Map<String, Object> row : topProducts) {
            promptBuilder.append(String.format("%s | %s | %s\n",
                    row.get("productName"), row.get("totalCount"), row.get("totalAmount")));
        }

        // 优质客户
        promptBuilder.append("\n【近30天优质客户TOP3】\n");
        promptBuilder.append("客户名称 | 订单数量 | 消费金额\n");
        for (Map<String, Object> row : topCustomers) {
            promptBuilder.append(String.format("%s | %s | %s\n",
                    row.get("customerName"), row.get("orderCount"), row.get("totalAmount")));
        }

        promptBuilder.append("\n请进行销售趋势分析，返回严格的JSON格式，\n");
        promptBuilder.append("不要返回任何其他文字，只返回JSON：\n");
        promptBuilder.append("{\n");
        promptBuilder.append("  \"trendType\": 趋势类型(1=上升 2=下降 3=平稳),\n");
        promptBuilder.append("  \"trendContent\": \"趋势分析内容(150字以内)\",\n");
        promptBuilder.append("  \"suggestContent\": \"销售提升建议(150字以内)\",\n");
        promptBuilder.append("  \"topProduct\": [\"热销产品1\", \"热销产品2\", \"热销产品3\"],\n");
        promptBuilder.append("  \"topCustomer\": [\"优质客户1\", \"优质客户2\", \"优质客户3\"]\n");
        promptBuilder.append("}\n");

        // 5. 调用 AI
        String responseContent;
        try {
            log.info("[generateAiAnalysis] 开始调用AI大模型，prompt长度={}", promptBuilder.length());
            responseContent = chatClient.prompt()
                    .user(promptBuilder.toString())
                    .call()
                    .content();
            log.info("[generateAiAnalysis] AI大模型返回成功，响应长度={}", responseContent != null ? responseContent.length() : 0);
        } catch (Exception e) {
            log.error("[generateAiAnalysis] 调用AI大模型失败", e);
            throw ServiceExceptionUtil.exception0(0, "AI生成销售分析失败，请稍后重试: " + e.getMessage());
        }

        // 6. 解析 JSON
        String jsonStr = extractJsonFromMarkdown(responseContent);
        log.info("[generateAiAnalysis] AI原始返回: {}", responseContent);
        JSONObject jsonResult;
        try {
            jsonResult = JSONUtil.parseObj(jsonStr);
            log.info("[generateAiAnalysis] JSON解析成功, keys={}", jsonResult.keySet());
        } catch (Exception e) {
            log.error("[generateAiAnalysis] 解析AI返回的JSON失败: {}", jsonStr, e);
            throw ServiceExceptionUtil.exception0(0, "AI返回格式错误，请重新生成");
        }

        // 7. 计算当月数据
        String currentMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        BigDecimal currentMonthAmount = BigDecimal.ZERO;
        int currentMonthOrderCount = 0;
        for (Map<String, Object> row : monthlyTrend) {
            if (currentMonth.equals(row.get("month"))) {
                currentMonthAmount = new BigDecimal(row.get("amount").toString());
                currentMonthOrderCount = ((Number) row.get("orderCount")).intValue();
                break;
            }
        }

        // 8. 保存分析结果
        ErpSaleAiAnalysisDO analysisDO = new ErpSaleAiAnalysisDO();
        analysisDO.setAnalysisMonth(currentMonth);
        analysisDO.setTotalSaleAmount(currentMonthAmount);
        analysisDO.setTotalOrderCount(currentMonthOrderCount);
        analysisDO.setTopProduct(jsonResult.getJSONArray("topProduct") != null
                ? jsonResult.getJSONArray("topProduct").toString() : "[]");
        analysisDO.setTopCustomer(jsonResult.getJSONArray("topCustomer") != null
                ? jsonResult.getJSONArray("topCustomer").toString() : "[]");
        analysisDO.setTrendType(jsonResult.getInt("trendType", 3));
        analysisDO.setTrendContent(jsonResult.getStr("trendContent", ""));
        analysisDO.setSuggestContent(jsonResult.getStr("suggestContent", ""));

        aiAnalysisMapper.insert(analysisDO);

        return BeanUtils.toBean(analysisDO, ErpSaleAiAnalysisRespVO.class);
    }

    @Override
    public ErpSaleAiAnalysisRespVO getLatestAnalysis() {
        ErpSaleAiAnalysisDO analysisDO = aiAnalysisMapper.selectOne(
                new LambdaQueryWrapper<ErpSaleAiAnalysisDO>()
                        .orderByDesc(ErpSaleAiAnalysisDO::getCreateTime)
                        .last("LIMIT 1")
        );
        if (analysisDO == null) {
            return null;
        }
        return BeanUtils.toBean(analysisDO, ErpSaleAiAnalysisRespVO.class);
    }

    @Override
    public List<ErpSaleAiAnalysisRespVO> getHistoryAnalysis() {
        List<ErpSaleAiAnalysisDO> list = aiAnalysisMapper.selectList(
                new LambdaQueryWrapper<ErpSaleAiAnalysisDO>()
                        .orderByDesc(ErpSaleAiAnalysisDO::getCreateTime)
        );
        return BeanUtils.toBean(list, ErpSaleAiAnalysisRespVO.class);
    }

    @Override
    public List<Map<String, Object>> getMonthlyTrend() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixMonthsAgo = now.minusMonths(6);
        return aiAnalysisMapper.selectMonthlyTrend(sixMonthsAgo, now);
    }

    private String extractJsonFromMarkdown(String content) {
        if (StrUtil.isBlank(content)) {
            return "{}";
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

}
