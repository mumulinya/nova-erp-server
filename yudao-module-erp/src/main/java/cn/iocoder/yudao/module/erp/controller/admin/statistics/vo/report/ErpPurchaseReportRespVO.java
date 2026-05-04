package cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 采购汇总报表行 Response VO")
@Data
public class ErpPurchaseReportRespVO {
    @Schema(description = "供应商编号")
    private Long supplierId;
    @Schema(description = "供应商名称")
    private String supplierName;
    @Schema(description = "采购入库金额")
    private BigDecimal inPrice;
    @Schema(description = "采购退货金额")
    private BigDecimal returnPrice;
    @Schema(description = "净采购金额")
    private BigDecimal netPrice;
}
