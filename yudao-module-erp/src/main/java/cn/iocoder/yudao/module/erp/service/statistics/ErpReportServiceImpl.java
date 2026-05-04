package cn.iocoder.yudao.module.erp.service.statistics;

import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpLogisticsCostReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpPurchaseReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpSaleReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpStockAlertRespVO;
import cn.nova.erp.module.erp.controller.admin.statistics.vo.report.*;
import cn.iocoder.yudao.module.erp.dal.mysql.statistics.ErpReportMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ERP 报表 Service 实现类
 */
@Service
public class ErpReportServiceImpl implements ErpReportService {

    @Resource
    private ErpReportMapper reportMapper;

    @Override
    public List<ErpPurchaseReportRespVO> getPurchaseReport(LocalDateTime beginTime, LocalDateTime endTime, Long supplierId) {
        return reportMapper.getPurchaseReport(beginTime, endTime, supplierId);
    }

    @Override
    public List<ErpSaleReportRespVO> getSaleReport(LocalDateTime beginTime, LocalDateTime endTime, Long customerId) {
        return reportMapper.getSaleReport(beginTime, endTime, customerId);
    }

    @Override
    public List<ErpStockAlertRespVO> getStockAlertReport() {
        return reportMapper.getStockAlertReport();
    }

    @Override
    public List<ErpLogisticsCostReportRespVO> getLogisticsCostReport(LocalDateTime beginTime, LocalDateTime endTime) {
        return reportMapper.getLogisticsCostReport(beginTime, endTime);
    }
}
