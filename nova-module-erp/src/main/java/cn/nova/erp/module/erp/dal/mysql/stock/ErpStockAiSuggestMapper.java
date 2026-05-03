package cn.nova.erp.module.erp.dal.mysql.stock;

import cn.nova.erp.framework.mybatis.core.mapper.BaseMapperX;
import cn.nova.erp.module.erp.dal.dataobject.stock.ErpStockAiSuggestDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AI 库存优化建议 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpStockAiSuggestMapper extends BaseMapperX<ErpStockAiSuggestDO> {

    /**
     * 查询库存统计数据（用于AI分析）
     * 包括：产品ID、产品名称、仓库ID、仓库名称、当前库存、安全库存、最大库存
     */
    List<Map<String, Object>> selectStockStatsForAi();

    /**
     * 查询指定时间段内的出库统计数据
     */
    List<Map<String, Object>> selectStockOutStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询最新一批建议（按创建时间过滤，取最后一次生成的所有记录）
     */
    List<ErpStockAiSuggestDO> selectLatestSuggests();

}
