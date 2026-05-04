package cn.iocoder.yudao.module.erp.dal.mysql.logistics;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsOrderDO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsOrderPageReqVO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogisticsOrderMapper extends BaseMapperX<LogisticsOrderDO> {

    default PageResult<LogisticsOrderDO> selectPage(LogisticsOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LogisticsOrderDO>()
                .likeIfPresent(LogisticsOrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(LogisticsOrderDO::getVehicleId, reqVO.getVehicleId())
                .eqIfPresent(LogisticsOrderDO::getStatus, reqVO.getStatus())
                .orderByDesc(LogisticsOrderDO::getId));
    }
}
