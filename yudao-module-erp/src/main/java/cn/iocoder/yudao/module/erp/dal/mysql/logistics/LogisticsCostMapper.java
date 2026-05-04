package cn.iocoder.yudao.module.erp.dal.mysql.logistics;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsCostDO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsCostPageReqVO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogisticsCostMapper extends BaseMapperX<LogisticsCostDO> {

    default PageResult<LogisticsCostDO> selectPage(LogisticsCostPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LogisticsCostDO>()
                .eqIfPresent(LogisticsCostDO::getLogisticsOrderId, reqVO.getLogisticsOrderId())
                .eqIfPresent(LogisticsCostDO::getSettlementStatus, reqVO.getSettlementStatus())
                .orderByDesc(LogisticsCostDO::getId));
    }
}
