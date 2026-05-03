package cn.nova.erp.module.erp.controller.admin.logistics.vo;

import cn.nova.erp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 车辆管理 分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LogisticsVehiclePageReqVO extends PageParam {

    @Schema(description = "车牌号")
    private String plateNo;

    @Schema(description = "车辆类型")
    private Integer vehicleType;

    @Schema(description = "车辆状态")
    private Integer status;
}
