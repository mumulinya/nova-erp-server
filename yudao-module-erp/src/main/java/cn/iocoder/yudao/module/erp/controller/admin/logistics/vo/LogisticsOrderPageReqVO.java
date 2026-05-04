package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 运输订单 分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LogisticsOrderPageReqVO extends PageParam {

    @Schema(description = "运输单号")
    private String orderNo;

    @Schema(description = "车辆ID")
    private Long vehicleId;

    @Schema(description = "运输状态")
    private Integer status;
}
