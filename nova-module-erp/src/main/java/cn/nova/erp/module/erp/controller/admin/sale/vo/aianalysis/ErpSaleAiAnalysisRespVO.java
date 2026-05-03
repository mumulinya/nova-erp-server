package cn.nova.erp.module.erp.controller.admin.sale.vo.aianalysis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP AI 销售趋势分析 Response VO")
@Data
public class ErpSaleAiAnalysisRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "分析月份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01")
    private String analysisMonth;

    @Schema(description = "当月销售总额", example = "100000.00")
    private BigDecimal totalSaleAmount;

    @Schema(description = "当月订单总数", example = "50")
    private Integer totalOrderCount;

    @Schema(description = "热销产品TOP3（JSON）", example = "[\"iPhone 15\",\"MacBook Pro\"]")
    private String topProduct;

    @Schema(description = "优质客户TOP3（JSON）", example = "[\"客户A\",\"客户B\"]")
    private String topCustomer;

    @Schema(description = "趋势类型 1上升 2下降 3平稳", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer trendType;

    @Schema(description = "AI趋势分析内容", example = "近6个月销售额持续上升...")
    private String trendContent;

    @Schema(description = "AI销售建议内容", example = "建议加大热销产品的库存储备...")
    private String suggestContent;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
