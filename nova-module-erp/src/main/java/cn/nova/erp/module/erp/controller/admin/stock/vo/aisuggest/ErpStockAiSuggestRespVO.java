package cn.nova.erp.module.erp.controller.admin.stock.vo.aisuggest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP AI 库存优化建议 Response VO")
@Data
public class ErpStockAiSuggestRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long productId;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "iPhone 15")
    private String productName;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long warehouseId;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海一号仓")
    private String warehouseName;

    @Schema(description = "当前库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Integer currentStock;

    @Schema(description = "安全库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    private Integer safetyStock;

    @Schema(description = "最大库存", example = "200")
    private Integer maxStock;

    @Schema(description = "日均销量", example = "1.5")
    private BigDecimal avgDailySale;

    @Schema(description = "建议类型 1补货 2清仓 3调拨 4正常", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer suggestType;

    @Schema(description = "AI优化建议内容", example = "库存低于安全库存且销量稳定，建议尽快补货")
    private String suggestContent;

    @Schema(description = "优先级 1紧急 2普通 3低", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer priority;

    @Schema(description = "状态 0待处理 1已处理 2已忽略", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
