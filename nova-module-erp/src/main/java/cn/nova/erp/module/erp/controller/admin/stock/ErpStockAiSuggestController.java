package cn.nova.erp.module.erp.controller.admin.stock;

import cn.nova.erp.framework.common.pojo.CommonResult;
import cn.nova.erp.module.erp.controller.admin.stock.vo.aisuggest.ErpStockAiSuggestRespVO;
import cn.nova.erp.module.erp.service.stock.ErpStockAiSuggestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.nova.erp.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP AI 库存优化建议")
@RestController
@RequestMapping("/erp/stock/ai-suggest")
@Validated
public class ErpStockAiSuggestController {

    @Resource
    private ErpStockAiSuggestService aiSuggestService;

    @PostMapping("/generate")
    @Operation(summary = "生成 AI 库存优化建议")
    @PreAuthorize("@ss.hasPermission('erp:stock:ai-suggest:query')")
    public CommonResult<List<ErpStockAiSuggestRespVO>> generateAiSuggest() {
        return success(aiSuggestService.generateAiSuggest());
    }

    @GetMapping("/latest")
    @Operation(summary = "获取最新一批 AI 库存优化建议")
    @PreAuthorize("@ss.hasPermission('erp:stock:ai-suggest:query')")
    public CommonResult<List<ErpStockAiSuggestRespVO>> getLatestAiSuggest() {
        return success(aiSuggestService.getLatestAiSuggest());
    }

    @PutMapping("/handle/{id}")
    @Operation(summary = "标记建议为已处理")
    @Parameter(name = "id", description = "建议编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:stock:ai-suggest:query')")
    public CommonResult<Boolean> handleAiSuggest(@PathVariable("id") Long id) {
        aiSuggestService.handleAiSuggest(id);
        return success(true);
    }

    @PutMapping("/ignore/{id}")
    @Operation(summary = "标记建议为已忽略")
    @Parameter(name = "id", description = "建议编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:stock:ai-suggest:query')")
    public CommonResult<Boolean> ignoreAiSuggest(@PathVariable("id") Long id) {
        aiSuggestService.ignoreAiSuggest(id);
        return success(true);
    }

}
