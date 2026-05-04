package cn.nova.erp.module.erp.controller.admin.logistics;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsRouteDO;
import cn.nova.erp.module.erp.service.logistics.LogisticsRouteService;

@Tag(name = "管理后台 - 配送路线")
@RestController
@RequestMapping("/erp/logistics-route")
@Validated
public class LogisticsRouteController {

    @Resource
    private LogisticsRouteService service;

    @PostMapping("/create")
    @Operation(summary = "创建配送路线")
    @PreAuthorize("@ss.hasPermission('erp:logistics-route:create')")
    public CommonResult<Long> createLogisticsRoute(@Valid @RequestBody LogisticsRouteSaveReqVO createReqVO) {
        return success(service.createLogisticsRoute(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新配送路线")
    @PreAuthorize("@ss.hasPermission('erp:logistics-route:update')")
    public CommonResult<Boolean> updateLogisticsRoute(@Valid @RequestBody LogisticsRouteSaveReqVO updateReqVO) {
        service.updateLogisticsRoute(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除配送路线")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-route:delete')")
    public CommonResult<Boolean> deleteLogisticsRoute(@RequestParam("id") Long id) {
        service.deleteLogisticsRoute(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得配送路线")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-route:query')")
    public CommonResult<LogisticsRouteRespVO> getLogisticsRoute(@RequestParam("id") Long id) {
        LogisticsRouteDO data = service.getLogisticsRoute(id);
        return success(BeanUtils.toBean(data, LogisticsRouteRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得配送路线分页")
    @PreAuthorize("@ss.hasPermission('erp:logistics-route:query')")
    public CommonResult<PageResult<LogisticsRouteRespVO>> getLogisticsRoutePage(@Valid LogisticsRoutePageReqVO pageVO) {
        PageResult<LogisticsRouteDO> pageResult = service.getLogisticsRoutePage(pageVO);
        return success(BeanUtils.toBean(pageResult, LogisticsRouteRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出配送路线 Excel")
    @PreAuthorize("@ss.hasPermission('erp:logistics-route:export')")
    public void exportLogisticsRoute(@Valid LogisticsRoutePageReqVO pageVO, HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<LogisticsRouteDO> pageResult = service.getLogisticsRoutePage(pageVO);
        // 转换成 VO
        List<LogisticsRouteRespVO> list = BeanUtils.toBean(pageResult.getList(), LogisticsRouteRespVO.class);
        // 导出
        ExcelUtils.write(response, "配送路线.xls", "数据", LogisticsRouteRespVO.class, list);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得配送路线精简列表", description = "用于运输订单下拉选择")
    @PreAuthorize("@ss.hasPermission('erp:logistics-route:query')")
    public CommonResult<List<LogisticsRouteRespVO>> getLogisticsRouteSimpleList() {
        List<LogisticsRouteDO> list = service.getLogisticsRouteList();
        return success(BeanUtils.toBean(list, LogisticsRouteRespVO.class));
    }
}
