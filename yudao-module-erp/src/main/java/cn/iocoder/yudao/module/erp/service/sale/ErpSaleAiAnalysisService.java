package cn.iocoder.yudao.module.erp.service.sale;

import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.aianalysis.ErpSaleAiAnalysisRespVO;

import java.util.List;
import java.util.Map;

/**
 * AI 销售趋势分析 Service 接口
 */
public interface ErpSaleAiAnalysisService {

    /**
     * 生成 AI 销售分析报告
     *
     * @return 销售分析报告
     */
    ErpSaleAiAnalysisRespVO generateAiAnalysis();

    /**
     * 获取最新的一份分析报告
     *
     * @return 最新的销售分析报告
     */
    ErpSaleAiAnalysisRespVO getLatestAnalysis();

    /**
     * 获取历史分析报告列表
     *
     * @return 历史销售分析报告列表
     */
    List<ErpSaleAiAnalysisRespVO> getHistoryAnalysis();

    /**
     * 获取近 6 个月的月度销售趋势
     *
     * @return 月度销售趋势数据
     */
    List<Map<String, Object>> getMonthlyTrend();

}

