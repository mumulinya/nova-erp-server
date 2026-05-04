package cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 销售汇总报表行 Response VO")
@Data
public class ErpSaleReportRespVO {
    @Schema(description = "客户编号")
    private Long customerId;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "销售出库金额")
    private BigDecimal outPrice;
    @Schema(description = "销售退货金额")
    private BigDecimal returnPrice;
    @Schema(description = "净销售金额")
    private BigDecimal netPrice;
}
