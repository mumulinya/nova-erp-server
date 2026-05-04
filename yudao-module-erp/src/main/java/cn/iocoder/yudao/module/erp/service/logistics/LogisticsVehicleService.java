package cn.iocoder.yudao.module.erp.service.logistics;

import java.util.*;

import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsVehiclePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsVehicleSaveReqVO;
import jakarta.validation.*;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsVehicleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

public interface LogisticsVehicleService {

    Long createLogisticsVehicle(@Valid LogisticsVehicleSaveReqVO createReqVO);

    void updateLogisticsVehicle(@Valid LogisticsVehicleSaveReqVO updateReqVO);

    void deleteLogisticsVehicle(Long id);

    LogisticsVehicleDO getLogisticsVehicle(Long id);

    PageResult<LogisticsVehicleDO> getLogisticsVehiclePage(LogisticsVehiclePageReqVO pageReqVO);

    /**
     * 获取车辆列表（用于运输订单下拉选择）
     */
    List<LogisticsVehicleDO> getLogisticsVehicleList();

    /**
     * 查找一辆空闲车辆（状态为 0）
     *
     * @return 空闲车辆，如果没有返回 null
     */
    LogisticsVehicleDO findAvailableVehicle();

    /**
     * 更新车辆状态
     *
     * @param vehicleId 车辆ID
     * @param status 状态（0空闲 1运输中 2维修中）
     */
    void updateVehicleStatus(Long vehicleId, Integer status);
}
