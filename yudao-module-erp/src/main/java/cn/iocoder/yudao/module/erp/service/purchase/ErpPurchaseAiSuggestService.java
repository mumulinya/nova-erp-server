package cn.iocoder.yudao.module.erp.service.purchase;

import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.aisuggest.ErpPurchaseAiSuggestRespVO;

import java.util.List;

/**
 * AI采购建议 Service 接口
 */
public interface ErpPurchaseAiSuggestService {

    /**
     * 生成 AI 采购建议
     *
     * @return 采购建议列表
     */
    List<ErpPurchaseAiSuggestRespVO> generateAiSuggest();

    /**
     * 获取最新 AI 采购建议
     *
     * @return 最新的采购建议列表
     */
    List<ErpPurchaseAiSuggestRespVO> getLatestAiSuggest();

    /**
     * 确认 AI 采购建议并生成采购订单
     *
     * @param id 建议 ID
     */
    void confirmAiSuggest(Long id);

    /**
     * 忽略 AI 采购建议
     *
     * @param id 建议 ID
     */
    void ignoreAiSuggest(Long id);

    /**
     * 获取 AI 采购建议详情
     *
     * @param id 建议 ID
     * @return AI 采购建议详情
     */
    ErpPurchaseAiSuggestRespVO getAiSuggest(Long id);

    /**
     * 删除 AI 采购建议
     *
     * @param id 建议 ID
     */
    void deleteAiSuggest(Long id);

}

