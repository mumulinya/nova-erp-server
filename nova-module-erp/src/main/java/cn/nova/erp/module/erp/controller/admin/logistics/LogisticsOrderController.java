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
import java.time.LocalDateTime;
import java.util.List;

import cn.nova.erp.framework.excel.core.util.ExcelUtils;
import cn.nova.erp.framework.common.pojo.CommonResult;
import cn.nova.erp.framework.common.pojo.PageParam;
import cn.nova.erp.framework.common.pojo.PageResult;
import cn.nova.erp.framework.common.util.object.BeanUtils;
import static cn.nova.erp.framework.common.pojo.CommonResult.success;

import cn.nova.erp.module.erp.controller.admin.logistics.vo.*;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsOrderDO;
import cn.nova.erp.module.erp.service.logistics.LogisticsOrderService;
import cn.nova.erp.module.erp.service.logistics.LogisticsAiDispatchService;

@Tag(name = "管理后台 - 运输订单")
@RestController
@RequestMapping("/erp/logistics-order")
@Validated
public class LogisticsOrderController {

    @Resource
    private LogisticsOrderService service;
    @Resource
    private LogisticsAiDispatchService aiDispatchService;

    @PostMapping("/create")
    @Operation(summary = "创建运输订单")
    @PreAuthorize("@ss.hasPermission('erp:logistics-order:create')")
    public CommonResult<Long> createLogisticsOrder(@Valid @RequestBody LogisticsOrderSaveReqVO createReqVO) {
        return success(service.createLogisticsOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新运输订单")
    @PreAuthorize("@ss.hasPermission('erp:logistics-order:update')")
    public CommonResult<Boolean> updateLogisticsOrder(@Valid @RequestBody LogisticsOrderSaveReqVO updateReqVO) {
        service.updateLogisticsOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除运输订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-order:delete')")
    public CommonResult<Boolean> deleteLogisticsOrder(@RequestParam("id") Long id) {
        service.deleteLogisticsOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得运输订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-order:query')")
    public CommonResult<LogisticsOrderRespVO> getLogisticsOrder(@RequestParam("id") Long id) {
        LogisticsOrderDO data = service.getLogisticsOrder(id);
        LogisticsOrderRespVO respVO = BeanUtils.toBean(data, LogisticsOrderRespVO.class);
        fillCanDispatch(respVO);
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得运输订单分页")
    @PreAuthorize("@ss.hasPermission('erp:logistics-order:query')")
    public CommonResult<PageResult<LogisticsOrderRespVO>> getLogisticsOrderPage(@Valid LogisticsOrderPageReqVO pageVO) {
        PageResult<LogisticsOrderDO> pageResult = service.getLogisticsOrderPage(pageVO);
        PageResult<LogisticsOrderRespVO> voPage = BeanUtils.toBean(pageResult, LogisticsOrderRespVO.class);
        if (voPage.getList() != null) {
            voPage.getList().forEach(this::fillCanDispatch);
        }
        return success(voPage);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得运输订单精简信息列表")
    public CommonResult<List<LogisticsOrderRespVO>> getLogisticsOrderSimpleList() {
        List<LogisticsOrderDO> list = service.getLogisticsOrderList();
        return success(BeanUtils.toBean(list, LogisticsOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出运输订单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:logistics-order:export')")
    public void exportLogisticsOrder(@Valid LogisticsOrderPageReqVO pageVO, HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<LogisticsOrderDO> pageResult = service.getLogisticsOrderPage(pageVO);
        // 转换成 VO
        List<LogisticsOrderRespVO> list = BeanUtils.toBean(pageResult.getList(), LogisticsOrderRespVO.class);
        if (list != null) {
            list.forEach(this::fillCanDispatch);
        }
        // 导出
        ExcelUtils.write(response, "运输订单.xls", "数据", LogisticsOrderRespVO.class, list);
    }

    @PostMapping("/ai-dispatch")
    @Operation(summary = "AI 智能调度")
    @Parameter(name = "id", description = "运输订单编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-order:update')")
    public CommonResult<Boolean> aiDispatch(@RequestParam("id") Long id) {
        aiDispatchService.autoDispatch(id, "仓库中心");
        return success(true);
    }

    /**
     * 填充“是否可发货”标志：当前时间 >= 出库时间，或者没有设置出库时间（手动创建的订单）时允许发货
     */
    private void fillCanDispatch(LogisticsOrderRespVO vo) {
        if (vo == null) {
            return;
        }
        if (vo.getOutTime() == null) {
            vo.setCanDispatch(true); // 没有出库时间限制，随时可发
        } else {
            vo.setCanDispatch(!LocalDateTime.now().isBefore(vo.getOutTime()));
        }
    }
}
