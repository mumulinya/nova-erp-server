package cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.aisuggest;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP AI采购建议 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpPurchaseAiSuggestRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long productId;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "iPhone 15")
    @ExcelProperty("产品名称")
    private String productName;

    @Schema(description = "当前库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @ExcelProperty("当前库存")
    private Integer currentStock;

    @Schema(description = "安全库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @ExcelProperty("安全库存")
    private Integer safetyStock;

    @Schema(description = "建议采购数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "15")
    @ExcelProperty("建议采购数量")
    private Integer suggestCount;

    @Schema(description = "推荐供应商", example = "苹果官方")
    @ExcelProperty("推荐供应商")
    private String supplierName;

    @Schema(description = "建议采购时间")
    @ExcelProperty("建议采购时间")
    private LocalDate suggestTime;

    @Schema(description = "AI分析原因", example = "库存即将耗尽，近期销量猛增")
    @ExcelProperty("AI分析原因")
    private String reason;

    @Schema(description = "状态（0待确认 1已转订单 2已忽略）", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
