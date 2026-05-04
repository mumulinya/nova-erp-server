package cn.iocoder.yudao.module.erp.service.ai;

import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardKpiRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardLogisticsStatusRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardStockHealthRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardTrendRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.*;
import cn.iocoder.yudao.module.erp.service.statistics.ErpDashboardService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ERP AI 供应链分析 Service
 * 独立的 AI 分析模块，负责收集大屏数据并调用大模型进行流式分析
 */
@Service
@Slf4j
public class ErpAiAnalysisService {

    @Value("${spring.ai.openai.api-key:}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.base-url:}")
    private String baseUrl;

    @Value("${spring.ai.openai.chat.completions-path:/v1/chat/completions}")
    private String completionsPath;

    @Value("${spring.ai.openai.chat.options.model:GLM-4.7}")
    private String modelName;

    @Resource
    private ErpDashboardService dashboardService;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 创建 SSE 流式分析
     */
    public SseEmitter streamAnalysis() {
        SseEmitter emitter = new SseEmitter(120_000L); // 2分钟超时

        executor.execute(() -> {
            try {
                // 1. 收集大屏各维度的实时数据
                String dataSnapshot = collectDataSnapshot();
                log.info("[AI分析] 已收集数据快照, 长度: {}", dataSnapshot.length());

                // 2. 构建 Prompt
                String prompt = buildAnalysisPrompt(dataSnapshot);

                // 3. 调用大模型 API（流式）
                callStreamApi(prompt, emitter);

            } catch (Exception e) {
                log.error("[AI分析] 流式分析异常", e);
                try {
                    emitter.send(SseEmitter.event().data("[ERROR] 分析异常：" + e.getMessage()));
                    emitter.complete();
                } catch (Exception ignored) {}
            }
        });

        return emitter;
    }

    /**
     * 收集大屏各维度的实时数据快照
     */
    private String collectDataSnapshot() {
        StringBuilder sb = new StringBuilder();

        // 核心指标
        ErpDashboardKpiRespVO kpi = dashboardService.getKpi();
        sb.append("【核心指标】\n");
        sb.append("- 今日订单数: ").append(kpi.getTodayOrderCount()).append("\n");
        sb.append("- 今日出库数: ").append(kpi.getTodayOutCount()).append("\n");
        sb.append("- 在途运输数: ").append(kpi.getTransportingCount()).append("\n");
        sb.append("- 库存预警数: ").append(kpi.getStockAlertCount()).append("\n\n");

        // 库存健康度
        ErpDashboardStockHealthRespVO health = dashboardService.getStockHealth();
        sb.append("【库存健康度】\n");
        sb.append("- 正常库存产品数: ").append(health.getNormalCount()).append("\n");
        sb.append("- 预警库存产品数: ").append(health.getAlertCount()).append("\n");
        sb.append("- 缺货产品数: ").append(health.getOutOfStockCount()).append("\n\n");

        // 物流状态
        List<ErpDashboardLogisticsStatusRespVO> logisticsStatus = dashboardService.getLogisticsStatus();
        sb.append("【物流状态分布】\n");
        for (ErpDashboardLogisticsStatusRespVO status : logisticsStatus) {
            sb.append("- ").append(status.getStatusName()).append(": ").append(status.getCount()).append("\n");
        }
        sb.append("\n");

        // 近30天销售趋势
        List<ErpDashboardTrendRespVO> saleTrend = dashboardService.getSaleTrend();
        sb.append("【近30天销售趋势（日期: 金额）】\n");
        for (ErpDashboardTrendRespVO item : saleTrend) {
            sb.append("- ").append(item.getDate()).append(": ").append(item.getValue()).append("元\n");
        }
        sb.append("\n");

        // 近30天采购趋势
        List<ErpDashboardTrendRespVO> purchaseTrend = dashboardService.getPurchaseTrend();
        sb.append("【近30天采购趋势（日期: 金额）】\n");
        for (ErpDashboardTrendRespVO item : purchaseTrend) {
            sb.append("- ").append(item.getDate()).append(": ").append(item.getValue()).append("元\n");
        }

        return sb.toString();
    }

    /**
     * 构建分析 Prompt
     */
    private String buildAnalysisPrompt(String dataSnapshot) {
        return "你是一位资深的供应链管理分析专家。以下是一个ERP系统供应链总览大屏的实时数据快照：\n\n" +
                dataSnapshot + "\n\n" +
                "请基于以上数据，从以下几个维度进行深入分析并生成供应链健康度报告（使用 Markdown 格式）：\n\n" +
                "1. **供应链整体健康度评估**（给出评分 1-100 和等级）\n" +
                "2. **销售趋势分析**（近期走势、环比变化、异常波动）\n" +
                "3. **库存风险预警**（预警和缺货情况的严重程度、建议补货策略）\n" +
                "4. **物流运营效率**（在途运输、异常订单、配送效率）\n" +
                "5. **采购建议**（基于销售趋势和库存情况给出采购建议）\n" +
                "6. **总结与行动建议**（列出 3-5 条可立即执行的改进措施）\n\n" +
                "请尽量用数据说话，结论要具体可操作。";
    }

    /**
     * 调用大模型 API（SSE 流式请求）
     */
    private void callStreamApi(String prompt, SseEmitter emitter) {
        String requestUrl = baseUrl + completionsPath;

        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", modelName);
        requestBody.set("stream", true);
        requestBody.set("temperature", 0.7);

        List<JSONObject> messages = new ArrayList<>();
        JSONObject systemMsg = new JSONObject();
        systemMsg.set("role", "system");
        systemMsg.set("content", "你是一位资深的供应链管理分析专家，擅长数据分析和业务洞察。");
        messages.add(systemMsg);

        JSONObject userMsg = new JSONObject();
        userMsg.set("role", "user");
        userMsg.set("content", prompt);
        messages.add(userMsg);
        requestBody.set("messages", messages);

        HttpURLConnection conn = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "text/event-stream");
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(120000);

            // 写入请求体
            byte[] bodyBytes = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            conn.getOutputStream().write(bodyBytes);
            conn.getOutputStream().flush();

            // 读取 SSE 流
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data: ")) {
                    String data = line.substring(6).trim();
                    if ("[DONE]".equals(data)) {
                        break;
                    }
                    try {
                        JSONObject chunk = JSONUtil.parseObj(data);
                        String content = chunk.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("delta")
                                .getStr("content", "");
                        if (!content.isEmpty()) {
                            emitter.send(SseEmitter.event().data(content));
                        }
                    } catch (Exception e) {
                        // 忽略解析错误的行
                    }
                }
            }

            reader.close();
            emitter.complete();
            log.info("[AI分析] 流式分析完成");

        } catch (Exception e) {
            log.error("[AI分析] 流式请求异常", e);
            try {
                emitter.send(SseEmitter.event().data("[ERROR] " + e.getMessage()));
                emitter.complete();
            } catch (Exception ignored) {}
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
