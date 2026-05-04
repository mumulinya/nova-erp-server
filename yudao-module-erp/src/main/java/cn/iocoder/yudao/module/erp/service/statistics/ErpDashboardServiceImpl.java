package cn.iocoder.yudao.module.erp.service.statistics;

import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardKpiRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardLogisticsStatusRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardStockHealthRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardTrendRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.*;
import cn.iocoder.yudao.module.erp.dal.mysql.statistics.ErpDashboardStatisticsMapper;
import cn.iocoder.yudao.module.erp.enums.logistics.LogisticsOrderStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ERP 大屏统计 Service 实现类
 */
@Service
public class ErpDashboardServiceImpl implements ErpDashboardService {

    @Resource
    private ErpDashboardStatisticsMapper dashboardStatisticsMapper;

    @Override
    public ErpDashboardKpiRespVO getKpi() {
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        ErpDashboardKpiRespVO kpi = new ErpDashboardKpiRespVO();
        kpi.setTodayOrderCount(dashboardStatisticsMapper.getTodayOrderCount(todayStart));
        kpi.setTodayOutCount(dashboardStatisticsMapper.getTodayOutCount(todayStart));
        kpi.setTransportingCount(dashboardStatisticsMapper.getTransportingCount());
        kpi.setStockAlertCount(dashboardStatisticsMapper.getStockAlertCount());
        return kpi;
    }

    @Override
    public List<ErpDashboardTrendRespVO> getSaleTrend() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30).with(LocalTime.MIN);
        return dashboardStatisticsMapper.getSaleTrend(startTime);
    }

    @Override
    public ErpDashboardStockHealthRespVO getStockHealth() {
        ErpDashboardStockHealthRespVO health = new ErpDashboardStockHealthRespVO();
        health.setNormalCount(dashboardStatisticsMapper.getStockNormalCount());
        health.setAlertCount(dashboardStatisticsMapper.getStockAlertProductCount());
        health.setOutOfStockCount(dashboardStatisticsMapper.getStockOutOfStockCount());
        return health;
    }

    @Override
    public List<ErpDashboardLogisticsStatusRespVO> getLogisticsStatus() {
        List<ErpDashboardLogisticsStatusRespVO> list = new ArrayList<>();
        for (LogisticsOrderStatusEnum status : LogisticsOrderStatusEnum.values()) {
            ErpDashboardLogisticsStatusRespVO vo = new ErpDashboardLogisticsStatusRespVO();
            vo.setStatusName(status.getName());
            vo.setCount(dashboardStatisticsMapper.getLogisticsCountByStatus(status.getStatus()));
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<ErpDashboardTrendRespVO> getPurchaseTrend() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30).with(LocalTime.MIN);
        return dashboardStatisticsMapper.getPurchaseTrend(startTime);
    }

    @Override
    public List<ErpDashboardTrendRespVO> getLogisticsCostTrend() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30).with(LocalTime.MIN);
        return dashboardStatisticsMapper.getLogisticsCostTrend(startTime);
    }

}
