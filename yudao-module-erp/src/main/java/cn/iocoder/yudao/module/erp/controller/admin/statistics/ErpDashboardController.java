package cn.iocoder.yudao.module.erp.controller.admin.statistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardKpiRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardLogisticsStatusRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardStockHealthRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardTrendRespVO;
import cn.nova.erp.module.erp.controller.admin.statistics.vo.dashboard.*;
import cn.nova.erp.module.erp.service.statistics.ErpDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP 供应链大屏")
@RestController
@RequestMapping("/erp/dashboard")
@Validated
public class ErpDashboardController {

    @Resource
    private ErpDashboardService dashboardService;

    @GetMapping("/kpi")
    @Operation(summary = "获取核心指标")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<ErpDashboardKpiRespVO> getKpi() {
        return success(dashboardService.getKpi());
    }

    @GetMapping("/sale-trend")
    @Operation(summary = "近30天销售趋势")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpDashboardTrendRespVO>> getSaleTrend() {
        return success(dashboardService.getSaleTrend());
    }

    @GetMapping("/stock-health")
    @Operation(summary = "库存健康度")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<ErpDashboardStockHealthRespVO> getStockHealth() {
        return success(dashboardService.getStockHealth());
    }

    @GetMapping("/logistics-status")
    @Operation(summary = "物流状态分布")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpDashboardLogisticsStatusRespVO>> getLogisticsStatus() {
        return success(dashboardService.getLogisticsStatus());
    }

    @GetMapping("/purchase-trend")
    @Operation(summary = "近30天采购趋势")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpDashboardTrendRespVO>> getPurchaseTrend() {
        return success(dashboardService.getPurchaseTrend());
    }

    @GetMapping("/logistics-cost-trend")
    @Operation(summary = "物流费用汇总趋势")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpDashboardTrendRespVO>> getLogisticsCostTrend() {
        return success(dashboardService.getLogisticsCostTrend());
    }

}
