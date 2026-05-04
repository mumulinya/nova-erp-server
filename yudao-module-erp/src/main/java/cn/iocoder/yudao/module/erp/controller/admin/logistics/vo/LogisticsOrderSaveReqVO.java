package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 运输订单新增/修改 Request VO")
@Data
public class LogisticsOrderSaveReqVO {

    @Schema(description = "运输单编号")
    private Long id;
    @Schema(description = "运输单号")
    private String orderNo;
    @Schema(description = "销售订单ID")
    private Long saleOrderId;
    @Schema(description = "销售订单号")
    private String saleOrderNo;
    @Schema(description = "收货人姓名")
    private String receiverName;
    @Schema(description = "收货人电话")
    private String receiverMobile;
    @Schema(description = "收货详细地址")
    private String receiverAddress;
    @Schema(description = "货物信息")
    private String goodsInfo;
    @Schema(description = "时效要求")
    private java.time.LocalDateTime timeRequirement;
    @Schema(description = "车辆ID")
    private Long vehicleId;
    @Schema(description = "路线ID")
    private Long routeId;
    @Schema(description = "出库时间")
    private java.time.LocalDateTime outTime;
    @Schema(description = "运输总费用(元)")
    private java.math.BigDecimal totalCost;
    @Schema(description = "运输状态")
    private Integer status;
    @Schema(description = "备注")
    private String remark;

}
