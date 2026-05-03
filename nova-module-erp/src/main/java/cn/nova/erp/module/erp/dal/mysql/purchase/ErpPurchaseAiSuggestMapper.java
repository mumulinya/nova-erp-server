package cn.nova.erp.module.erp.dal.mysql.purchase;

import cn.nova.erp.framework.mybatis.core.mapper.BaseMapperX;
import cn.nova.erp.module.erp.dal.dataobject.purchase.ErpPurchaseAiSuggestDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AI采购建议 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpPurchaseAiSuggestMapper extends BaseMapperX<ErpPurchaseAiSuggestDO> {

    /**
     * 获取低于安全库存的产品列表
     */
    List<Map<String, Object>> selectNeedReplenishProducts();

    /**
     * 批量查询产品在指定时间段内的销量
     */
    List<Map<String, Object>> selectProductSaleCounts(@Param("productIds") List<Long> productIds, 
                                                      @Param("startTime") LocalDateTime startTime, 
                                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 批量查询产品在指定时间段内的最新一条采购记录（供应商、价格等）
     */
    List<Map<String, Object>> selectProductRecentPurchaseRecords(@Param("productIds") List<Long> productIds, 
                                                                 @Param("startTime") LocalDateTime startTime, 
                                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 获取最新一批的建议记录
     */
    List<ErpPurchaseAiSuggestDO> selectLatestSuggests();

}
