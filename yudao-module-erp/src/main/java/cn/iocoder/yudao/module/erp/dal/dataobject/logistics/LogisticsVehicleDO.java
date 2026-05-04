package cn.iocoder.yudao.module.erp.dal.dataobject.logistics;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 车辆管理 DO
 */
@TableName("logistics_vehicle")
@KeySequence("logistics_vehicle_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsVehicleDO extends BaseDO {

    /**
     * 车辆编号
     */
    private Long id;
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 车辆类型
     */
    private Integer vehicleType;
    /**
     * 司机姓名
     */
    private String driverName;
    /**
     * 司机手机号
     */
    private String driverMobile;
    /**
     * 最大载重
     */
    private java.math.BigDecimal maxWeight;
    /**
     * 最大容积
     */
    private java.math.BigDecimal maxVolume;
    /**
     * 车辆状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
