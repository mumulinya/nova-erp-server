package cn.nova.erp.module.erp.controller.admin.statistics.vo.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - ERP 大屏库存健康度 Response VO")
@Data
public class ErpDashboardStockHealthRespVO {

    @Schema(description = "正常库存产品数")
    private Integer normalCount;

    @Schema(description = "预警库存产品数（库存 > 0 且 < 安全库存）")
    private Integer alertCount;

    @Schema(description = "缺货产品数（库存 <= 0）")
    private Integer outOfStockCount;

}
