package cn.iocoder.yudao.module.erp.service.statistics;

import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpLogisticsCostReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpPurchaseReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpSaleReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpStockAlertRespVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ERP 报表 Service 接口
 */
public interface ErpReportService {

    /**
     * 获取采购报表数据
     */
    List<ErpPurchaseReportRespVO> getPurchaseReport(LocalDateTime beginTime, LocalDateTime endTime, Long supplierId);

    /**
     * 获取销售报表数据
     */
    List<ErpSaleReportRespVO> getSaleReport(LocalDateTime beginTime, LocalDateTime endTime, Long customerId);

    /**
     * 获取库存预警报表
     */
    List<ErpStockAlertRespVO> getStockAlertReport();

    /**
     * 获取物流费用明细报表
     */
    List<ErpLogisticsCostReportRespVO> getLogisticsCostReport(LocalDateTime beginTime, LocalDateTime endTime);

}

