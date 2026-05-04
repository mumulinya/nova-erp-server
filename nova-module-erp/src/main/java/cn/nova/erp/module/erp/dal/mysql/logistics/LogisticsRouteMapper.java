package cn.nova.erp.module.erp.dal.mysql.logistics;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsRouteDO;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.LogisticsRoutePageReqVO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogisticsRouteMapper extends BaseMapperX<LogisticsRouteDO> {

    default PageResult<LogisticsRouteDO> selectPage(LogisticsRoutePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LogisticsRouteDO>()
                .likeIfPresent(LogisticsRouteDO::getName, reqVO.getName())
                .likeIfPresent(LogisticsRouteDO::getStartAddress, reqVO.getStartAddress())
                .likeIfPresent(LogisticsRouteDO::getEndAddress, reqVO.getEndAddress())
                .orderByDesc(LogisticsRouteDO::getId));
    }
}
