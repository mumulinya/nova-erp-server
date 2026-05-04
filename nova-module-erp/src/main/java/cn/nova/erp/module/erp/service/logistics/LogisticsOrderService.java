package cn.nova.erp.module.erp.service.logistics;

import java.util.*;
import jakarta.validation.*;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

public interface LogisticsOrderService {

    Long createLogisticsOrder(@Valid LogisticsOrderSaveReqVO createReqVO);

    void updateLogisticsOrder(@Valid LogisticsOrderSaveReqVO updateReqVO);

    void deleteLogisticsOrder(Long id);

    LogisticsOrderDO getLogisticsOrder(Long id);

    PageResult<LogisticsOrderDO> getLogisticsOrderPage(LogisticsOrderPageReqVO pageReqVO);

    /**
     * 获取运输订单列表（用于其他模块下拉选择）
     */
    List<LogisticsOrderDO> getLogisticsOrderList();
}
