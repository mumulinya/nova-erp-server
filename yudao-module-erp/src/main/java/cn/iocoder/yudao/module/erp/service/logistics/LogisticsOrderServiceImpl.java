package cn.iocoder.yudao.module.erp.service.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsOrderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsOrderSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsCostDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsOrderDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsRouteDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsVehicleDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.context.annotation.Lazy;
import jakarta.annotation.Resource;
import java.util.*;
import java.time.LocalDateTime;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.*;
import cn.iocoder.yudao.module.erp.dal.mysql.logistics.LogisticsOrderMapper;
import cn.iocoder.yudao.module.erp.service.sale.ErpSaleOutService;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
@Validated
public class LogisticsOrderServiceImpl implements LogisticsOrderService {

    @Resource
    private LogisticsOrderMapper mapper;

    @Resource
    @Lazy
    private ErpSaleOutService saleOutService;

    @Resource
    private LogisticsVehicleService vehicleService;

    @Resource
    private LogisticsRouteService routeService;

    @Resource
    @Lazy
    private LogisticsCostService costService;

    @Override
    public Long createLogisticsOrder(LogisticsOrderSaveReqVO createReqVO) {
        LogisticsOrderDO data = BeanUtils.toBean(createReqVO, LogisticsOrderDO.class);
        mapper.insert(data);
        return data.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLogisticsOrder(LogisticsOrderSaveReqVO updateReqVO) {
        // 1. 获取更新前的订单信息
        LogisticsOrderDO oldOrder = mapper.selectById(updateReqVO.getId());
        Integer oldStatus = oldOrder != null ? oldOrder.getStatus() : null;
        Integer newStatus = updateReqVO.getStatus();

        // 2. 更新订单
        LogisticsOrderDO updateObj = BeanUtils.toBean(updateReqVO, LogisticsOrderDO.class);
        mapper.updateById(updateObj);

        // 3. 处理状态变更带来的车辆联动
        if (newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) {
            LogisticsOrderDO order = mapper.selectById(updateReqVO.getId());

            // 3.1 发货（0→1）：校验出库时间 + 车辆分配
            if (newStatus == 1) {
                // ★ 核心校验：如果设置了出库时间，当前时间必须 >= 出库时间才允许发货
                if (order.getOutTime() != null && LocalDateTime.now().isBefore(order.getOutTime())) {
                    throw exception(new cn.iocoder.yudao.framework.common.exception.ErrorCode(
                            1_030_100_001, "未到出库时间（" + order.getOutTime() + "），暂不能发货"));
                }
                Long vehicleId = order.getVehicleId();
                if (vehicleId == null) {
                    // 尝试自动分配空闲车辆
                    LogisticsVehicleDO vehicle = vehicleService.findAvailableVehicle();
                    if (vehicle != null) {
                        vehicleId = vehicle.getId();
                        // 更新订单的车辆ID
                        LogisticsOrderDO vehicleUpdate = new LogisticsOrderDO();
                        vehicleUpdate.setId(order.getId());
                        vehicleUpdate.setVehicleId(vehicleId);
                        mapper.updateById(vehicleUpdate);
                    }
                }
                // 将车辆状态设为运输中
                if (vehicleId != null) {
                    vehicleService.updateVehicleStatus(vehicleId, 1); // 1=运输中
                }
            }

            // 3.2 签收（1→2）：释放车辆，车辆状态改回空闲
            if (newStatus == 2 && order.getVehicleId() != null) {
                vehicleService.updateVehicleStatus(order.getVehicleId(), 0); // 0=空闲
            }

            // 4. 同步更新关联的出库单物流状态
            if (order.getSaleOrderId() != null) {
                saleOutService.updateSaleOutLogisticsStatus(order.getSaleOrderId(), newStatus);
            }
        }
    }

    @Override
    public void deleteLogisticsOrder(Long id) {
        // 删除前，如果有关联车辆且状态为运输中，释放车辆
        LogisticsOrderDO order = mapper.selectById(id);
        if (order != null && order.getVehicleId() != null && order.getStatus() != null && order.getStatus() == 1) {
            vehicleService.updateVehicleStatus(order.getVehicleId(), 0); // 释放车辆
        }
        mapper.deleteById(id);
    }

    @Override
    public LogisticsOrderDO getLogisticsOrder(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<LogisticsOrderDO> getLogisticsOrderPage(LogisticsOrderPageReqVO pageReqVO) {
        PageResult<LogisticsOrderDO> pageResult = mapper.selectPage(pageReqVO);
        if (pageResult.getList().isEmpty()) {
            return pageResult;
        }

        // 1. 计算是否可以发货
        LocalDateTime now = LocalDateTime.now();
        pageResult.getList().forEach(order -> {
            order.setCanDispatch(order.getOutTime() == null || now.isAfter(order.getOutTime()));
        });

        // 2. 补全车辆和路线名称
        // 2.1 获取关联 ID
        Set<Long> vehicleIds = convertSet(pageResult.getList(), LogisticsOrderDO::getVehicleId);
        Set<Long> routeIds = convertSet(pageResult.getList(), LogisticsOrderDO::getRouteId);
        // 2.2 查询关联数据
        Map<Long, LogisticsVehicleDO> vehicleMap = new HashMap<>();
        if (!vehicleIds.isEmpty()) {
            List<LogisticsVehicleDO> vehicles = vehicleService.getLogisticsVehicleList();
            vehicleMap = convertMap(vehicles, LogisticsVehicleDO::getId);
        }
        Map<Long, LogisticsRouteDO> routeMap = new HashMap<>();
        if (!routeIds.isEmpty()) {
            List<LogisticsRouteDO> routes = routeService.getLogisticsRouteList();
            routeMap = convertMap(routes, LogisticsRouteDO::getId);
        }
        // 2.3 查询费用关联数据
        Set<Long> orderIds = convertSet(pageResult.getList(), LogisticsOrderDO::getId);
        Set<Long> ordersWithCost = new HashSet<>();
        if (!orderIds.isEmpty()) {
            List<LogisticsCostDO> costs = costService.getLogisticsCostListByOrderIds(orderIds);
            ordersWithCost = convertSet(costs, LogisticsCostDO::getLogisticsOrderId);
        }

        // 2.4 填充数据
        for (LogisticsOrderDO order : pageResult.getList()) {
            LogisticsVehicleDO vehicle = vehicleMap.get(order.getVehicleId());
            if (vehicle != null) {
                order.setPlateNo(vehicle.getPlateNo());
            }
            LogisticsRouteDO route = routeMap.get(order.getRouteId());
            if (route != null) {
                order.setRouteName(route.getStartAddress() + " → " + route.getEndAddress());
            }
            order.setHasCost(ordersWithCost.contains(order.getId()));
        }

        return pageResult;
    }

    @Override
    public List<LogisticsOrderDO> getLogisticsOrderList() {
        return mapper.selectList();
    }
}
