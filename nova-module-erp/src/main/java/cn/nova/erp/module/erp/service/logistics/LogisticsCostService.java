package cn.nova.erp.module.erp.service.logistics;

import java.util.*;
import jakarta.validation.*;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsCostDO;
import cn.nova.erp.framework.common.pojo.PageResult;

public interface LogisticsCostService {

    Long createLogisticsCost(@Valid LogisticsCostSaveReqVO createReqVO);

    void updateLogisticsCost(@Valid LogisticsCostSaveReqVO updateReqVO);

    void deleteLogisticsCost(Long id);

    LogisticsCostDO getLogisticsCost(Long id);

    PageResult<LogisticsCostDO> getLogisticsCostPage(LogisticsCostPageReqVO pageReqVO);

    /**
     * 根据订单ID集合获取关联的费用记录列表
     */
    List<LogisticsCostDO> getLogisticsCostListByOrderIds(Collection<Long> orderIds);
}
