package cn.nova.erp.module.erp.service.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.annotation.Resource;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsCostDO;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsOrderDO;
import cn.nova.erp.module.erp.dal.mysql.logistics.LogisticsCostMapper;
import java.util.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
@Validated
public class LogisticsCostServiceImpl implements LogisticsCostService {

    @Resource
    private LogisticsCostMapper mapper;

    @Resource
    private LogisticsOrderService orderService;

    @Override
    public Long createLogisticsCost(LogisticsCostSaveReqVO createReqVO) {
        // 校验：是否已存在关联该运输订单的费用记录
        LogisticsCostDO existingCost = mapper.selectOne(LogisticsCostDO::getLogisticsOrderId, createReqVO.getLogisticsOrderId());
        if (existingCost != null) {
            throw exception(new cn.iocoder.yudao.framework.common.exception.ErrorCode(1_030_200_001, "该运输订单已存在关联的费用记录，无法重复录入"));
        }
        
        LogisticsCostDO data = BeanUtils.toBean(createReqVO, LogisticsCostDO.class);
        mapper.insert(data);
        return data.getId();
    }

    @Override
    public void updateLogisticsCost(LogisticsCostSaveReqVO updateReqVO) {
        LogisticsCostDO updateObj = BeanUtils.toBean(updateReqVO, LogisticsCostDO.class);
        mapper.updateById(updateObj);
    }

    @Override
    public void deleteLogisticsCost(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public LogisticsCostDO getLogisticsCost(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<LogisticsCostDO> getLogisticsCostPage(LogisticsCostPageReqVO pageReqVO) {
        PageResult<LogisticsCostDO> pageResult = mapper.selectPage(pageReqVO);
        if (pageResult.getList().isEmpty()) {
            return pageResult;
        }
        
        Set<Long> orderIds = convertSet(pageResult.getList(), LogisticsCostDO::getLogisticsOrderId);
        if (!orderIds.isEmpty()) {
            // Usually we'd have a method getLogisticsOrderList(Collection ids) but since I don't know if it exists,
            // I will fetch all or iterate if there are few. For robustness I'll iterate.
            Map<Long, LogisticsOrderDO> orderMap = new HashMap<>();
            for (Long orderId : orderIds) {
                LogisticsOrderDO order = orderService.getLogisticsOrder(orderId);
                if (order != null) {
                    orderMap.put(orderId, order);
                }
            }
            for (LogisticsCostDO cost : pageResult.getList()) {
                LogisticsOrderDO order = orderMap.get(cost.getLogisticsOrderId());
                if (order != null) {
                    cost.setOrderNo(order.getOrderNo());
                    cost.setReceiverName(order.getReceiverName());
                    cost.setReceiverAddress(order.getReceiverAddress());
                }
            }
        }
        
        return pageResult;
    }

    @Override
    public List<LogisticsCostDO> getLogisticsCostListByOrderIds(Collection<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        return mapper.selectList(LogisticsCostDO::getLogisticsOrderId, orderIds);
    }
}
