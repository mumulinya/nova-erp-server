package cn.iocoder.yudao.module.erp.service.logistics;

import java.util.*;

import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsOrderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsOrderSaveReqVO;
import jakarta.validation.*;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsOrderDO;
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
