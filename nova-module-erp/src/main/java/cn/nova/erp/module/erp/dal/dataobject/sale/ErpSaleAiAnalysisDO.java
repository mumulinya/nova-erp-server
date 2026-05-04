package cn.nova.erp.module.erp.dal.dataobject.sale;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * AI 销售趋势分析 DO
 *
 * @author 芋道源码
 */
@TableName("erp_sale_ai_analysis")
@KeySequence("erp_sale_ai_analysis_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpSaleAiAnalysisDO extends BaseDO {

    /** 主键 */
    @TableId
    private Long id;
    /** 分析月份 yyyy-MM */
    private String analysisMonth;
    /** 当月销售总额 */
    private BigDecimal totalSaleAmount;
    /** 当月订单总数 */
    private Integer totalOrderCount;
    /** 热销产品TOP3（JSON） */
    private String topProduct;
    /** 优质客户TOP3（JSON） */
    private String topCustomer;
    /** 趋势类型 1上升 2下降 3平稳 */
    private Integer trendType;
    /** AI趋势分析内容 */
    private String trendContent;
    /** AI销售建议内容 */
    private String suggestContent;

}
