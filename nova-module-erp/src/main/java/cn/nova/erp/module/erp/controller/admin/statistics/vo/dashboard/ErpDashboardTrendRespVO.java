package cn.nova.erp.module.erp.controller.admin.statistics.vo.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 大屏趋势数据 Response VO")
@Data
@Accessors(chain = true)
public class ErpDashboardTrendRespVO {

    @Schema(description = "日期，如 2026-04-01")
    private String date;

    @Schema(description = "金额")
    private BigDecimal value;

}
