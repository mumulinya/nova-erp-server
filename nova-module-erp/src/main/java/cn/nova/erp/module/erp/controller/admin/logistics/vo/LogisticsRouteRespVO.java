package cn.nova.erp.module.erp.controller.admin.logistics.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 配送路线 Response VO")
@Data
@ExcelIgnoreUnannotated
public class LogisticsRouteRespVO {

    @Schema(description = "路线编号")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "路线名称")
    @ExcelProperty("路线名称")
    private String name;

    @Schema(description = "起点地址")
    @ExcelProperty("起点地址")
    private String startAddress;

    @Schema(description = "终点地址")
    @ExcelProperty("终点地址")
    private String endAddress;

    @Schema(description = "路线距离")
    @ExcelProperty("距离(km)")
    private java.math.BigDecimal distance;

    @Schema(description = "预计时长")
    @ExcelProperty("预计时长(小时)")
    private java.math.BigDecimal estimatedHours;

    @Schema(description = "状态")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "AI路线建议")
    @ExcelProperty("AI路线建议")
    private String aiSuggestion;


    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
