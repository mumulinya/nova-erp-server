package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 车辆管理 Response VO")
@Data
public class LogisticsVehicleRespVO {

    @Schema(description = "车辆编号")
    private Long id;
    @Schema(description = "车牌号")
    private String plateNo;
    @Schema(description = "车辆类型")
    private Integer vehicleType;
    @Schema(description = "司机姓名")
    private String driverName;
    @Schema(description = "司机手机号")
    private String driverMobile;
    @Schema(description = "最大载重")
    private java.math.BigDecimal maxWeight;
    @Schema(description = "最大容积")
    private java.math.BigDecimal maxVolume;
    @Schema(description = "车辆状态")
    private Integer status;
    @Schema(description = "备注")
    private String remark;


    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
