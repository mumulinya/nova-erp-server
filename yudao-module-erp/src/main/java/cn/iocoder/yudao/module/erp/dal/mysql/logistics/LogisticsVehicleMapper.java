package cn.iocoder.yudao.module.erp.dal.mysql.logistics;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsVehicleDO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsVehiclePageReqVO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogisticsVehicleMapper extends BaseMapperX<LogisticsVehicleDO> {

    default PageResult<LogisticsVehicleDO> selectPage(LogisticsVehiclePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LogisticsVehicleDO>()
                .likeIfPresent(LogisticsVehicleDO::getPlateNo, reqVO.getPlateNo())
                .eqIfPresent(LogisticsVehicleDO::getVehicleType, reqVO.getVehicleType())
                .eqIfPresent(LogisticsVehicleDO::getStatus, reqVO.getStatus())
                .orderByDesc(LogisticsVehicleDO::getId));
    }
}
