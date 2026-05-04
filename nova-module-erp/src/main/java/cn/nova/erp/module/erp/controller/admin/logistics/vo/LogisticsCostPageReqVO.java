package cn.nova.erp.module.erp.controller.admin.logistics.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 运输费用 分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LogisticsCostPageReqVO extends PageParam {

    @Schema(description = "运输单ID")
    private Long logisticsOrderId;

    @Schema(description = "结算状态")
    private Integer settlementStatus;
}
