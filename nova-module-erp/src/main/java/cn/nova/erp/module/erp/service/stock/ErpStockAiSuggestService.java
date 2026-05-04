package cn.nova.erp.module.erp.service.stock;

import cn.nova.erp.module.erp.controller.admin.stock.vo.aisuggest.ErpStockAiSuggestRespVO;

import java.util.List;

/**
 * AI 库存优化建议 Service 接口
 */
public interface ErpStockAiSuggestService {

    /**
     * 生成库存优化建议
     *
     * @return 优化建议列表
     */
    List<ErpStockAiSuggestRespVO> generateAiSuggest();

    /**
     * 获取最新 AI 库存优化建议
     *
     * @return 最新的优化建议列表
     */
    List<ErpStockAiSuggestRespVO> getLatestAiSuggest();

    /**
     * 处理 AI 建议
     *
     * @param id 建议 ID
     */
    void handleAiSuggest(Long id);

    /**
     * 忽略 AI 建议
     *
     * @param id 建议 ID
     */
    void ignoreAiSuggest(Long id);

}
