package cn.nova.erp.module.infra.dal.mysql.db;

import cn.nova.erp.framework.mybatis.core.mapper.BaseMapperX;
import cn.nova.erp.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapperX<DataSourceConfigDO> {
}
