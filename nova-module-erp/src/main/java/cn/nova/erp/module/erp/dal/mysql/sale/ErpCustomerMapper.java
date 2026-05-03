package cn.nova.erp.module.erp.dal.mysql.sale;

import cn.nova.erp.framework.common.pojo.PageResult;
import cn.nova.erp.framework.mybatis.core.mapper.BaseMapperX;
import cn.nova.erp.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.nova.erp.module.erp.controller.admin.sale.vo.customer.ErpCustomerPageReqVO;
import cn.nova.erp.module.erp.dal.dataobject.sale.ErpCustomerDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ERP 客户 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpCustomerMapper extends BaseMapperX<ErpCustomerDO> {

    default PageResult<ErpCustomerDO> selectPage(ErpCustomerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpCustomerDO>()
                .likeIfPresent(ErpCustomerDO::getName, reqVO.getName())
                .eqIfPresent(ErpCustomerDO::getMobile, reqVO.getMobile())
                .eqIfPresent(ErpCustomerDO::getTelephone, reqVO.getTelephone())
                .orderByDesc(ErpCustomerDO::getId));
    }

    default List<ErpCustomerDO> selectListByStatus(Integer status) {
        return selectList(ErpCustomerDO::getStatus, status);
    }

}