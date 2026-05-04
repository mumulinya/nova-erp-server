package cn.iocoder.yudao.module.erp.controller.admin.logistics;

import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsVehiclePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsVehicleRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.LogisticsVehicleSaveReqVO;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.LogisticsVehicleDO;
import cn.iocoder.yudao.module.erp.service.logistics.LogisticsVehicleService;

import java.util.List;

@Tag(name = "管理后台 - 车辆管理")
@RestController
@RequestMapping("/erp/logistics-vehicle")
@Validated
public class LogisticsVehicleController {

    @Resource
    private LogisticsVehicleService service;

    @PostMapping("/create")
    @Operation(summary = "创建车辆管理")
    @PreAuthorize("@ss.hasPermission('erp:logistics-vehicle:create')")
    public CommonResult<Long> createLogisticsVehicle(@Valid @RequestBody LogisticsVehicleSaveReqVO createReqVO) {
        return success(service.createLogisticsVehicle(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新车辆管理")
    @PreAuthorize("@ss.hasPermission('erp:logistics-vehicle:update')")
    public CommonResult<Boolean> updateLogisticsVehicle(@Valid @RequestBody LogisticsVehicleSaveReqVO updateReqVO) {
        service.updateLogisticsVehicle(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除车辆管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-vehicle:delete')")
    public CommonResult<Boolean> deleteLogisticsVehicle(@RequestParam("id") Long id) {
        service.deleteLogisticsVehicle(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得车辆管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-vehicle:query')")
    public CommonResult<LogisticsVehicleRespVO> getLogisticsVehicle(@RequestParam("id") Long id) {
        LogisticsVehicleDO data = service.getLogisticsVehicle(id);
        return success(BeanUtils.toBean(data, LogisticsVehicleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得车辆管理分页")
    @PreAuthorize("@ss.hasPermission('erp:logistics-vehicle:query')")
    public CommonResult<PageResult<LogisticsVehicleRespVO>> getLogisticsVehiclePage(@Valid LogisticsVehiclePageReqVO pageVO) {
        PageResult<LogisticsVehicleDO> pageResult = service.getLogisticsVehiclePage(pageVO);
        return success(BeanUtils.toBean(pageResult, LogisticsVehicleRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得车辆精简列表", description = "用于运输订单下拉选择")
    @PreAuthorize("@ss.hasPermission('erp:logistics-vehicle:query')")
    public CommonResult<List<LogisticsVehicleRespVO>> getLogisticsVehicleSimpleList() {
        List<LogisticsVehicleDO> list = service.getLogisticsVehicleList();
        return success(BeanUtils.toBean(list, LogisticsVehicleRespVO.class));
    }
}
