package cn.nova.erp.module.erp.dal.mysql.sale;

import cn.nova.erp.framework.mybatis.core.mapper.BaseMapperX;
import cn.nova.erp.module.erp.dal.dataobject.sale.ErpSaleAiAnalysisDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AI 销售趋势分析 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpSaleAiAnalysisMapper extends BaseMapperX<ErpSaleAiAnalysisDO> {

    /**
     * 查询近6个月每月销售总额和订单数
     */
    List<Map<String, Object>> selectMonthlyTrend(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询近30天销售额TOP3产品
     */
    List<Map<String, Object>> selectTopProducts(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询近30天销售额TOP3客户
     */
    List<Map<String, Object>> selectTopCustomers(LocalDateTime startTime, LocalDateTime endTime);

}
