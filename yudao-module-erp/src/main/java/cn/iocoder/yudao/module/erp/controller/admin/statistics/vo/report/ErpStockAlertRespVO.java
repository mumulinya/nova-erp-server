package cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 库存预警报表行 Response VO")
@Data
public class ErpStockAlertRespVO {
    @Schema(description = "产品编号")
    private Long productId;
    @Schema(description = "产品名称")
    private String productName;
    @Schema(description = "仓库名称")
    private String warehouseName;
    @Schema(description = "当前库存")
    private BigDecimal currentStock;
    @Schema(description = "安全库存")
    private BigDecimal safeStock;
    @Schema(description = "缺口数量")
    private BigDecimal gapCount;
}
