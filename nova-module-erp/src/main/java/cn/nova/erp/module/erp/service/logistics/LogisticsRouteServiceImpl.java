package cn.nova.erp.module.erp.service.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.annotation.Resource;
import java.util.List;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsRouteDO;
import cn.nova.erp.module.erp.dal.mysql.logistics.LogisticsRouteMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
@Validated
public class LogisticsRouteServiceImpl implements LogisticsRouteService {

    @Resource
    private LogisticsRouteMapper mapper;

    @Override
    public Long createLogisticsRoute(LogisticsRouteSaveReqVO createReqVO) {
        LogisticsRouteDO data = BeanUtils.toBean(createReqVO, LogisticsRouteDO.class);
        mapper.insert(data);
        return data.getId();
    }

    @Override
    public void updateLogisticsRoute(LogisticsRouteSaveReqVO updateReqVO) {
        LogisticsRouteDO updateObj = BeanUtils.toBean(updateReqVO, LogisticsRouteDO.class);
        mapper.updateById(updateObj);
    }

    @Override
    public void deleteLogisticsRoute(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public LogisticsRouteDO getLogisticsRoute(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<LogisticsRouteDO> getLogisticsRoutePage(LogisticsRoutePageReqVO pageReqVO) {
        return mapper.selectPage(pageReqVO);
    }

    @Override
    public List<LogisticsRouteDO> getLogisticsRouteList() {
        return mapper.selectList();
    }
}
