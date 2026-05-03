package cn.nova.erp.module.erp.controller.admin.statistics.vo.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 物流费用汇总报表行 Response VO")
@Data
public class ErpLogisticsCostReportRespVO {
    @Schema(description = "运输单号")
    private String orderNo;
    @Schema(description = "运输费")
    private BigDecimal transportCost;
    @Schema(description = "燃油费")
    private BigDecimal fuelCost;
    @Schema(description = "过路费")
    private BigDecimal tollCost;
    @Schema(description = "其他费用")
    private BigDecimal otherCost;
    @Schema(description = "合计")
    private BigDecimal totalCost;
}
