package cn.iocoder.yudao.module.erp.dal.dataobject.logistics;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 运输订单 DO
 */
@TableName("logistics_order")
@KeySequence("logistics_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsOrderDO extends BaseDO {

    /**
     * 运输单编号
     */
    private Long id;
    /**
     * 运输单号
     */
    private String orderNo;
    /**
     * 销售订单ID
     */
    private Long saleOrderId;
    /**
     * 销售订单号
     */
    private String saleOrderNo;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人电话
     */
    private String receiverMobile;
    /**
     * 收货详细地址
     */
    private String receiverAddress;
    /**
     * 货物信息
     */
    private String goodsInfo;
    /**
     * 时效要求
     */
    private java.time.LocalDateTime timeRequirement;
    /**
     * 车辆ID
     */
    private Long vehicleId;
    /**
     * 路线ID
     */
    private Long routeId;
    /**
     * 出库时间（从关联的销售出库单带入）
     * 只有当前时间 >= outTime 时，才允许发货
     */
    private java.time.LocalDateTime outTime;
    /**
     * 是否可发货（逻辑字段，不存库）
     */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private Boolean canDispatch;
    /**
     * 关联展示：车牌号
     */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String plateNo;
    /**
     * 关联展示：路线名称（起点-终点）
     */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String routeName;
    /**
     * 是否已录入费用（逻辑字段，不存库）
     */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private Boolean hasCost;
    /**
     * 运输总费用(元)
     */
    private java.math.BigDecimal totalCost;
    /**
     * 运输状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
