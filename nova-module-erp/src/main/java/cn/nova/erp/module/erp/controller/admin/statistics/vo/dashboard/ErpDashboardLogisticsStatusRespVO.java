package cn.nova.erp.module.erp.controller.admin.statistics.vo.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - ERP 大屏物流状态分布 Response VO")
@Data
@Accessors(chain = true)
public class ErpDashboardLogisticsStatusRespVO {

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "数量")
    private Integer count;

}
