package cn.nova.erp.module.erp.controller.admin.logistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 运输费用新增/修改 Request VO")
@Data
public class LogisticsCostSaveReqVO {

    @Schema(description = "费用单编号")
    private Long id;
    @Schema(description = "运输单ID")
    private Long logisticsOrderId;
    @Schema(description = "运输成本(元)")
    private java.math.BigDecimal transportCost;
    @Schema(description = "燃油费(元)")
    private java.math.BigDecimal fuelCost;
    @Schema(description = "过路费(元)")
    private java.math.BigDecimal tollCost;
    @Schema(description = "其他费用(元)")
    private java.math.BigDecimal otherCost;
    @Schema(description = "合计成本")
    private java.math.BigDecimal totalCost;
    @Schema(description = "结算状态")
    private Integer settlementStatus;
    @Schema(description = "备注")
    private String remark;

}
