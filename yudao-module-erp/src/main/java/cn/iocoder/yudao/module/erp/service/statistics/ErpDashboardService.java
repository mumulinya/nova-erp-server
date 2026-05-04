package cn.iocoder.yudao.module.erp.service.statistics;

import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardKpiRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardLogisticsStatusRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardStockHealthRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardTrendRespVO;

import java.util.List;

/**
 * ERP 大屏统计 Service 接口
 */
public interface ErpDashboardService {

    /**
     * 获取 KPI 数据
     */
    ErpDashboardKpiRespVO getKpi();

    /**
     * 获取近30天销售趋势
     */
    List<ErpDashboardTrendRespVO> getSaleTrend();

    /**
     * 获取库存健康状态
     */
    ErpDashboardStockHealthRespVO getStockHealth();

    /**
     * 获取各状态物流订单分布
     */
    List<ErpDashboardLogisticsStatusRespVO> getLogisticsStatus();

    /**
     * 获取近30天采购趋势
     */
    List<ErpDashboardTrendRespVO> getPurchaseTrend();

    /**
     * 获取近30天物流成本趋势
     */
    List<ErpDashboardTrendRespVO> getLogisticsCostTrend();

}

