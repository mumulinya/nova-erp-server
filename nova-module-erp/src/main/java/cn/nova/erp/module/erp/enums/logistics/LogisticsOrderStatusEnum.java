package cn.nova.erp.module.erp.enums.logistics;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

/**
 * 运输状态枚举
 */
@Getter
@AllArgsConstructor
public enum LogisticsOrderStatusEnum implements ArrayValuable<Integer> {

    WAIT_DISPATCH(0, "待调度"),
    TRANSPORTING(1, "运输中"),
    DELIVERED(2, "已送达"),
    EXCEPTION(3, "异常");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(LogisticsOrderStatusEnum::getStatus).toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
