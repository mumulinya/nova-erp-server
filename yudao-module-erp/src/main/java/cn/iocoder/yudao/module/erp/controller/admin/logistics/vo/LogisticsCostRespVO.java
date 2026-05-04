package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 运输费用 Response VO")
@Data
public class LogisticsCostRespVO {

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

    // --- 关联展示字段 ---
    @Schema(description = "关联运输单号")
    private String orderNo;
    @Schema(description = "客户名称(收货人)")
    private String receiverName;
    @Schema(description = "收货地址")
    private String receiverAddress;
    @Schema(description = "结算状态")
    private Integer settlementStatus;
    @Schema(description = "备注")
    private String remark;


    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
