package cn.nova.erp.module.erp.controller.admin.logistics;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import cn.nova.erp.framework.common.pojo.CommonResult;
import cn.nova.erp.framework.common.pojo.PageResult;
import cn.nova.erp.framework.common.util.object.BeanUtils;
import static cn.nova.erp.framework.common.pojo.CommonResult.success;

import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsCostDO;
import cn.nova.erp.module.erp.service.logistics.LogisticsCostService;

@Tag(name = "管理后台 - 运输费用")
@RestController
@RequestMapping("/erp/logistics-cost")
@Validated
public class LogisticsCostController {

    @Resource
    private LogisticsCostService service;

    @PostMapping("/create")
    @Operation(summary = "创建运输费用")
    @PreAuthorize("@ss.hasPermission('erp:logistics-cost:create')")
    public CommonResult<Long> createLogisticsCost(@Valid @RequestBody LogisticsCostSaveReqVO createReqVO) {
        return success(service.createLogisticsCost(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新运输费用")
    @PreAuthorize("@ss.hasPermission('erp:logistics-cost:update')")
    public CommonResult<Boolean> updateLogisticsCost(@Valid @RequestBody LogisticsCostSaveReqVO updateReqVO) {
        service.updateLogisticsCost(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除运输费用")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-cost:delete')")
    public CommonResult<Boolean> deleteLogisticsCost(@RequestParam("id") Long id) {
        service.deleteLogisticsCost(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得运输费用")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-cost:query')")
    public CommonResult<LogisticsCostRespVO> getLogisticsCost(@RequestParam("id") Long id) {
        LogisticsCostDO data = service.getLogisticsCost(id);
        return success(BeanUtils.toBean(data, LogisticsCostRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得运输费用分页")
    @PreAuthorize("@ss.hasPermission('erp:logistics-cost:query')")
    public CommonResult<PageResult<LogisticsCostRespVO>> getLogisticsCostPage(@Valid LogisticsCostPageReqVO pageVO) {
        PageResult<LogisticsCostDO> pageResult = service.getLogisticsCostPage(pageVO);
        return success(BeanUtils.toBean(pageResult, LogisticsCostRespVO.class));
    }
}
