package cn.iocoder.yudao.module.erp.dal.mysql.statistics;

import cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.dashboard.ErpDashboardTrendRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ERP 大屏统计 Mapper
 */
@Mapper
public interface ErpDashboardStatisticsMapper {

    /** 今日销售订单数 */
    Integer getTodayOrderCount(@Param("todayStart") LocalDateTime todayStart);

    /** 今日出库数 */
    Integer getTodayOutCount(@Param("todayStart") LocalDateTime todayStart);

    /** 在途运输数 */
    Integer getTransportingCount();

    /** 库存预警数（库存 < 安全库存 的产品去重数） */
    Integer getStockAlertCount();

    /** 近 N 天每日销售金额 */
    List<ErpDashboardTrendRespVO> getSaleTrend(@Param("startTime") LocalDateTime startTime);

    /** 库存健康度：正常数 */
    Integer getStockNormalCount();
    /** 库存健康度：预警数 */
    Integer getStockAlertProductCount();
    /** 库存健康度：缺货数 */
    Integer getStockOutOfStockCount();

    /** 物流状态分布 */
    Integer getLogisticsCountByStatus(@Param("status") Integer status);

    /** 近 N 天每日采购金额 */
    List<ErpDashboardTrendRespVO> getPurchaseTrend(@Param("startTime") LocalDateTime startTime);

    /** 近 N 天物流费用汇总趋势 */
    List<ErpDashboardTrendRespVO> getLogisticsCostTrend(@Param("startTime") LocalDateTime startTime);

}
