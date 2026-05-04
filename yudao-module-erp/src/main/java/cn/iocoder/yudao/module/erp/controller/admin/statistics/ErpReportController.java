package cn.iocoder.yudao.module.erp.controller.admin.statistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpLogisticsCostReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpPurchaseReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpSaleReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpStockAlertRespVO;
import cn.nova.erp.module.erp.controller.admin.statistics.vo.report.*;
import cn.nova.erp.module.erp.service.statistics.ErpReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Tag(name = "管理后台 - ERP 业务报表")
@RestController
@RequestMapping("/erp/report")
@Validated
public class ErpReportController {

    @Resource
    private ErpReportService reportService;

    @GetMapping("/purchase-summary")
    @Operation(summary = "采购汇总报表")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpPurchaseReportRespVO>> getPurchaseReport(
            @RequestParam(value = "beginTime", required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime beginTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime endTime,
            @RequestParam(value = "supplierId", required = false) Long supplierId) {
        return success(reportService.getPurchaseReport(beginTime, endTime, supplierId));
    }

    @GetMapping("/sale-summary")
    @Operation(summary = "销售汇总报表")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpSaleReportRespVO>> getSaleReport(
            @RequestParam(value = "beginTime", required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime beginTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime endTime,
            @RequestParam(value = "customerId", required = false) Long customerId) {
        return success(reportService.getSaleReport(beginTime, endTime, customerId));
    }

    @GetMapping("/stock-alert")
    @Operation(summary = "库存预警报表")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpStockAlertRespVO>> getStockAlertReport() {
        return success(reportService.getStockAlertReport());
    }

    @GetMapping("/logistics-cost-summary")
    @Operation(summary = "物流费用汇总报表")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<ErpLogisticsCostReportRespVO>> getLogisticsCostReport(
            @RequestParam(value = "beginTime", required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime beginTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime endTime) {
        return success(reportService.getLogisticsCostReport(beginTime, endTime));
    }

}
