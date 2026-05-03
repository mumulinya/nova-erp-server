package cn.nova.erp.module.erp.controller.admin.logistics.vo;

import cn.nova.erp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 配送路线 分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LogisticsRoutePageReqVO extends PageParam {

    @Schema(description = "路线名称")
    private String name;

    @Schema(description = "起点地址")
    private String startAddress;

    @Schema(description = "终点地址")
    private String endAddress;
}
