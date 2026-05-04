package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 运输订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class LogisticsOrderRespVO {

    @Schema(description = "运输单编号")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "运输单号")
    @ExcelProperty("运输单号")
    private String orderNo;

    @Schema(description = "销售订单ID")
    private Long saleOrderId;

    @Schema(description = "销售订单号")
    @ExcelProperty("销售订单号")
    private String saleOrderNo;

    @Schema(description = "收货人姓名")
    @ExcelProperty("收货人")
    private String receiverName;

    @Schema(description = "收货人电话")
    @ExcelProperty("收货人电话")
    private String receiverMobile;

    @Schema(description = "收货详细地址")
    @ExcelProperty("收货地址")
    private String receiverAddress;

    @Schema(description = "货物信息")
    @ExcelProperty("货物信息")
    private String goodsInfo;

    @Schema(description = "时效要求")
    @ExcelProperty("时效要求")
    private java.time.LocalDateTime timeRequirement;

    @Schema(description = "车辆ID")
    private Long vehicleId;

    @Schema(description = "路线ID")
    private Long routeId;

    @Schema(description = "出库时间")
    @ExcelProperty("出库时间")
    private LocalDateTime outTime;

    @Schema(description = "运输总费用(元)")
    @ExcelProperty("运输费用(元)")
    private java.math.BigDecimal totalCost;

    @Schema(description = "是否可发货（当前时间 >= 出库时间）")
    private Boolean canDispatch;

    @Schema(description = "车辆车牌号")
    @ExcelProperty("车牌号")
    private String plateNo;

    @Schema(description = "路线名称")
    @ExcelProperty("路线")
    private String routeName;

    @Schema(description = "是否已录入费用")
    private Boolean hasCost;

    @Schema(description = "运输状态")
    @ExcelProperty("运输状态")
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;


    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
