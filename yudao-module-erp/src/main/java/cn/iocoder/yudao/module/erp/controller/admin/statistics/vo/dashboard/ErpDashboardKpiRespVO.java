package cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - ERP 大屏核心指标 Response VO")
@Data
public class ErpDashboardKpiRespVO {

    @Schema(description = "今日订单数")
    private Integer todayOrderCount;

    @Schema(description = "今日出库数")
    private Integer todayOutCount;

    @Schema(description = "在途运输数")
    private Integer transportingCount;

    @Schema(description = "库存预警数")
    private Integer stockAlertCount;

}
