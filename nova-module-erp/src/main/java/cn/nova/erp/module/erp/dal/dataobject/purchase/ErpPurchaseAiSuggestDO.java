package cn.nova.erp.module.erp.dal.dataobject.purchase;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * AI采购建议 DO
 *
 * @author 芋道源码
 */
@TableName("erp_purchase_ai_suggest")
@KeySequence("erp_purchase_ai_suggest_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpPurchaseAiSuggestDO extends BaseDO {

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
     * 当前库存
     */
    private Integer currentStock;
    /**
     * 安全库存
     */
    private Integer safetyStock;
    /**
     * 建议采购数量
     */
    private Integer suggestCount;
    /**
     * 推荐供应商
     */
    private String supplierName;
    /**
     * 建议采购时间
     */
    private LocalDate suggestTime;
    /**
     * AI分析原因
     */
    private String reason;
    /**
     * 状态（0待确认 1已转订单 2已忽略）
     */
    private Integer status;
    /**
     * 生成的采购订单ID
     */
    private Long orderId;

}
