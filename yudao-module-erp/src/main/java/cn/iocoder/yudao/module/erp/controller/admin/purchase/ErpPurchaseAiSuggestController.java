package cn.iocoder.yudao.module.erp.controller.admin.purchase;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.aisuggest.ErpPurchaseAiSuggestRespVO;
import cn.iocoder.yudao.module.erp.service.purchase.ErpPurchaseAiSuggestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP AI采购建议")
@RestController
@RequestMapping("/erp/purchase/ai-suggest")
@Validated
public class ErpPurchaseAiSuggestController {

    @Resource
    private ErpPurchaseAiSuggestService aiSuggestService;

    @PostMapping("/generate")
    @Operation(summary = "生成AI采购建议")
    @PreAuthorize("@ss.hasPermission('erp:purchase:ai-suggest:query')")
    public CommonResult<List<ErpPurchaseAiSuggestRespVO>> generateAiSuggest() {
        return success(aiSuggestService.generateAiSuggest());
    }

    @GetMapping("/latest")
    @Operation(summary = "获取最新一批AI建议")
    @PreAuthorize("@ss.hasPermission('erp:purchase:ai-suggest:query')")
    public CommonResult<List<ErpPurchaseAiSuggestRespVO>> getLatestAiSuggest() {
        return success(aiSuggestService.getLatestAiSuggest());
    }

    @GetMapping("/get")
    @Operation(summary = "获取单条AI建议详情")
    @Parameter(name = "id", description = "建议编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:purchase:ai-suggest:query')")
    public CommonResult<ErpPurchaseAiSuggestRespVO> getAiSuggest(@RequestParam("id") Long id) {
        return success(aiSuggestService.getAiSuggest(id));
    }

    @PutMapping("/confirm/{id}")
    @Operation(summary = "将建议转为正式采购订单")
    @Parameter(name = "id", description = "建议编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:purchase:ai-suggest:query')")
    public CommonResult<Boolean> confirmAiSuggest(@PathVariable("id") Long id) {
        aiSuggestService.confirmAiSuggest(id);
        return success(true);
    }

    @PutMapping("/ignore/{id}")
    @Operation(summary = "忽略某条采购建议")
    @Parameter(name = "id", description = "建议编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:purchase:ai-suggest:query')")
    public CommonResult<Boolean> ignoreAiSuggest(@PathVariable("id") Long id) {
        aiSuggestService.ignoreAiSuggest(id);
        return success(true);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除某条采购建议")
    @Parameter(name = "id", description = "建议编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:purchase:ai-suggest:query')")
    public CommonResult<Boolean> deleteAiSuggest(@PathVariable("id") Long id) {
        aiSuggestService.deleteAiSuggest(id);
        return success(true);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出AI采购建议 Excel")
    @PreAuthorize("@ss.hasPermission('erp:purchase:ai-suggest:query')")
    public void exportAiSuggestExcel(HttpServletResponse response) throws IOException {
        List<ErpPurchaseAiSuggestRespVO> list = aiSuggestService.getLatestAiSuggest();
        ExcelUtils.write(response, "AI采购建议.xls", "数据", ErpPurchaseAiSuggestRespVO.class, list);
    }

}
