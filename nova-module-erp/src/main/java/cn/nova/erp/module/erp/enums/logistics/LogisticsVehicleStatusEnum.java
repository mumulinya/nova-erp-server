package cn.nova.erp.module.erp.enums.logistics;

import cn.nova.erp.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

/**
 * 车辆状态枚举
 */
@Getter
@AllArgsConstructor
public enum LogisticsVehicleStatusEnum implements ArrayValuable<Integer> {

    FREE(0, "空闲"),
    TRANSPORTING(1, "运输中"),
    MAINTAINING(2, "维修中");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(LogisticsVehicleStatusEnum::getStatus).toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
