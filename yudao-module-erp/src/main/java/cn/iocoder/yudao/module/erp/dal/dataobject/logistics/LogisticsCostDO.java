package cn.iocoder.yudao.module.erp.dal.dataobject.logistics;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 运输费用 DO
 */
@TableName("logistics_cost")
@KeySequence("logistics_cost_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsCostDO extends BaseDO {

    /**
     * 费用单编号
     */
    private Long id;
    /**
     * 运输单ID
     */
    private Long logisticsOrderId;
    /**
     * 运输成本(元)
     */
    private java.math.BigDecimal transportCost;
    /**
     * 燃油费(元)
     */
    private java.math.BigDecimal fuelCost;
    /**
     * 过路费(元)
     */
    private java.math.BigDecimal tollCost;
    /**
     * 其他费用(元)
     */
    private java.math.BigDecimal otherCost;
    /**
     * 合计成本
     */
    private java.math.BigDecimal totalCost;

    // --- 关联展示字段 ---
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String orderNo;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String receiverName;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String receiverAddress;

    /**
     * 结算状态
     */
    private Integer settlementStatus;
    /**
     * 备注
     */
    private String remark;

}
