package cn.iocoder.yudao.module.erp.controller.admin.ai;

import cn.iocoder.yudao.module.erp.service.ai.ErpAiAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "管理后台 - ERP AI 分析")
@RestController
@RequestMapping("/erp/ai-analysis")
@Validated
public class ErpAiAnalysisController {

    @Resource
    private ErpAiAnalysisService aiAnalysisService;

    @GetMapping(value = "/stream", produces = "text/event-stream;charset=UTF-8")
    @Operation(summary = "AI 供应链分析（流式）")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public SseEmitter streamAnalysis() {
        return aiAnalysisService.streamAnalysis();
    }

}
