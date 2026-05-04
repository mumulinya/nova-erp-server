package cn.iocoder.yudao.module.erp.dal.mysql.statistics;

import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpLogisticsCostReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpPurchaseReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpSaleReportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report.ErpStockAlertRespVO;
import cn.nova.erp.module.erp.controller.admin.statistics.vo.report.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ERP 报表统计 Mapper
 */
@Mapper
public interface ErpReportMapper {

    List<ErpPurchaseReportRespVO> getPurchaseReport(@Param("beginTime") LocalDateTime beginTime,
                                                    @Param("endTime") LocalDateTime endTime,
                                                    @Param("supplierId") Long supplierId);

    List<ErpSaleReportRespVO> getSaleReport(@Param("beginTime") LocalDateTime beginTime,
                                            @Param("endTime") LocalDateTime endTime,
                                            @Param("customerId") Long customerId);

    List<ErpStockAlertRespVO> getStockAlertReport();

    List<ErpLogisticsCostReportRespVO> getLogisticsCostReport(@Param("beginTime") LocalDateTime beginTime,
                                                              @Param("endTime") LocalDateTime endTime);
}
