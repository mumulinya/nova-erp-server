package cn.iocoder.yudao.module.erp.service.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsVehiclePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsVehicleSaveReqVO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.annotation.Resource;
import java.util.List;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsVehicleDO;
import cn.iocoder.yudao.module.erp.dal.mysql.logistics.LogisticsVehicleMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
@Validated
public class LogisticsVehicleServiceImpl implements LogisticsVehicleService {

    @Resource
    private LogisticsVehicleMapper mapper;

    @Override
    public Long createLogisticsVehicle(LogisticsVehicleSaveReqVO createReqVO) {
        LogisticsVehicleDO data = BeanUtils.toBean(createReqVO, LogisticsVehicleDO.class);
        mapper.insert(data);
        return data.getId();
    }

    @Override
    public void updateLogisticsVehicle(LogisticsVehicleSaveReqVO updateReqVO) {
        LogisticsVehicleDO updateObj = BeanUtils.toBean(updateReqVO, LogisticsVehicleDO.class);
        mapper.updateById(updateObj);
    }

    @Override
    public void deleteLogisticsVehicle(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public LogisticsVehicleDO getLogisticsVehicle(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<LogisticsVehicleDO> getLogisticsVehiclePage(LogisticsVehiclePageReqVO pageReqVO) {
        return mapper.selectPage(pageReqVO);
    }

    @Override
    public List<LogisticsVehicleDO> getLogisticsVehicleList() {
        return mapper.selectList();
    }

    @Override
    public LogisticsVehicleDO findAvailableVehicle() {
        return mapper.selectOne(new LambdaQueryWrapperX<LogisticsVehicleDO>()
                .eq(LogisticsVehicleDO::getStatus, 0) // 0=空闲
                .orderByAsc(LogisticsVehicleDO::getId)
                .last("LIMIT 1"));
    }

    @Override
    public void updateVehicleStatus(Long vehicleId, Integer status) {
        mapper.updateById(new LogisticsVehicleDO() {{
            setId(vehicleId);
            setStatus(status);
        }});
    }
}
