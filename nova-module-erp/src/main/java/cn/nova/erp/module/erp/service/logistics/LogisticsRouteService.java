package cn.nova.erp.module.erp.service.logistics;

import java.util.*;
import jakarta.validation.*;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsRouteDO;
import cn.nova.erp.framework.common.pojo.PageResult;

public interface LogisticsRouteService {

    Long createLogisticsRoute(@Valid LogisticsRouteSaveReqVO createReqVO);

    void updateLogisticsRoute(@Valid LogisticsRouteSaveReqVO updateReqVO);

    void deleteLogisticsRoute(Long id);

    LogisticsRouteDO getLogisticsRoute(Long id);

    PageResult<LogisticsRouteDO> getLogisticsRoutePage(LogisticsRoutePageReqVO pageReqVO);

    /**
     * 获取配送路线列表（用于运输订单下拉选择）
     */
    List<LogisticsRouteDO> getLogisticsRouteList();
}
