package cn.nova.erp.module.erp.dal.dataobject.stock;

import cn.nova.erp.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * AI 库存优化建议 DO
 *
 * @author 芋道源码
 */
@TableName("erp_stock_ai_suggest")
@KeySequence("erp_stock_ai_suggest_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpStockAiSuggestDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 当前库存
     */
    private Integer currentStock;
    /**
     * 安全库存
     */
    private Integer safetyStock;
    /**
     * 最大库存
     */
    private Integer maxStock;
    /**
     * 日均销量
     */
    private BigDecimal avgDailySale;
    /**
     * 建议类型 1补货 2清仓 3调拨 4正常
     */
    private Integer suggestType;
    /**
     * AI优化建议内容
     */
    private String suggestContent;
    /**
     * 优先级 1紧急 2普通 3低
     */
    private Integer priority;
    /**
     * 状态 0待处理 1已处理 2已忽略
     */
    private Integer status;

}
