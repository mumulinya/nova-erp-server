package cn.nova.erp.module.erp.controller.admin.sale;

import cn.nova.erp.framework.common.pojo.CommonResult;
import cn.nova.erp.module.erp.controller.admin.sale.vo.aianalysis.ErpSaleAiAnalysisRespVO;
import cn.nova.erp.module.erp.service.sale.ErpSaleAiAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static cn.nova.erp.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP AI 销售趋势分析")
@RestController
@RequestMapping("/erp/sale/ai-analysis")
@Validated
public class ErpSaleAiAnalysisController {

    @Resource
    private ErpSaleAiAnalysisService aiAnalysisService;

    @PostMapping("/generate")
    @Operation(summary = "生成 AI 销售趋势分析")
    @PreAuthorize("@ss.hasPermission('erp:sale:ai-analysis:query')")
    public CommonResult<ErpSaleAiAnalysisRespVO> generateAiAnalysis() {
        return success(aiAnalysisService.generateAiAnalysis());
    }

    @GetMapping("/latest")
    @Operation(summary = "获取最近一条分析记录")
    @PreAuthorize("@ss.hasPermission('erp:sale:ai-analysis:query')")
    public CommonResult<ErpSaleAiAnalysisRespVO> getLatestAnalysis() {
        return success(aiAnalysisService.getLatestAnalysis());
    }

    @GetMapping("/history")
    @Operation(summary = "获取历史分析记录列表")
    @PreAuthorize("@ss.hasPermission('erp:sale:ai-analysis:query')")
    public CommonResult<List<ErpSaleAiAnalysisRespVO>> getHistoryAnalysis() {
        return success(aiAnalysisService.getHistoryAnalysis());
    }

    @GetMapping("/monthly-trend")
    @Operation(summary = "获取近6个月每月销售额数据")
    @PreAuthorize("@ss.hasPermission('erp:sale:ai-analysis:query')")
    public CommonResult<List<Map<String, Object>>> getMonthlyTrend() {
        return success(aiAnalysisService.getMonthlyTrend());
    }

}
