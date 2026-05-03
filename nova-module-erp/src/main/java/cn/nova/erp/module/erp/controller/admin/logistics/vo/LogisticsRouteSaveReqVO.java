package cn.nova.erp.module.erp.controller.admin.logistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 配送路线新增/修改 Request VO")
@Data
public class LogisticsRouteSaveReqVO {

    @Schema(description = "路线编号")
    private Long id;
    @Schema(description = "路线名称")
    private String name;
    @Schema(description = "起点地址")
    private String startAddress;
    @Schema(description = "终点地址")
    private String endAddress;
    @Schema(description = "路线距离")
    private java.math.BigDecimal distance;
    @Schema(description = "预计时长")
    private java.math.BigDecimal estimatedHours;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "AI路线建议")
    private String aiSuggestion;

}
