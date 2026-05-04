package cn.iocoder.yudao.module.erp.dal.dataobject.logistics;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 配送路线 DO
 */
@TableName("logistics_route")
@KeySequence("logistics_route_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsRouteDO extends BaseDO {

    /**
     * 路线编号
     */
    private Long id;
    /**
     * 路线名称
     */
    private String name;
    /**
     * 起点地址
     */
    private String startAddress;
    /**
     * 终点地址
     */
    private String endAddress;
    /**
     * 路线距离
     */
    private java.math.BigDecimal distance;
    /**
     * 预计时长
     */
    private java.math.BigDecimal estimatedHours;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * AI路线建议
     */
    private String aiSuggestion;

}
