package cn.nova.erp.module.erp.service.logistics;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.LogisticsOrderSaveReqVO;
import cn.nova.erp.module.erp.controller.admin.logistics.vo.LogisticsRouteSaveReqVO;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsOrderDO;
import cn.nova.erp.module.erp.dal.dataobject.logistics.LogisticsVehicleDO;
import cn.nova.erp.module.erp.enums.logistics.LogisticsOrderStatusEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI 自动调度服务
 */
@Service
@Slf4j
public class LogisticsAiDispatchService {

    private ChatClient chatClient;

    @Resource(name = "openAiChatModel")
    private ChatModel chatModel;

    @Resource
    private LogisticsOrderService logisticsOrderService;

    @Resource
    private LogisticsVehicleService logisticsVehicleService;

    @Resource
    private LogisticsRouteService logisticsRouteService;

    @Resource
    private cn.nova.erp.module.erp.service.sale.ErpSaleOutService saleOutService;

    @Resource
    private cn.nova.erp.module.erp.service.stock.ErpWarehouseService warehouseService;

    @PostConstruct
    public void init() {
        this.chatClient = ChatClient.create(chatModel);
    }

    /**
     * 统一调用大模型 API 的方法（基于 Spring AI）
     */
    private String callThirdPartyApi(String promptText) {
        log.info("========== 开始调用大模型 API ==========");
        log.info("Prompt 摘要 (前200字): {}", promptText.length() > 200 ? promptText.substring(0, 200) + "..." : promptText);

        long startTime = System.currentTimeMillis();
        try {
            String content = chatClient.prompt()
                    .system("你是一个专业的物流调度和路线规划专家。")
                    .user(promptText)
                    .call()
                    .content();

            long costTime = System.currentTimeMillis() - startTime;
            log.info("大模型调用成功! 耗时: {}ms", costTime);
            log.info("AI 返回内容摘要 (前300字): {}", content.length() > 300 ? content.substring(0, 300) + "..." : content);
            log.info("========== 大模型调用结束 (成功) ==========");
            return content;
        } catch (Exception e) {
            long costTime = System.currentTimeMillis() - startTime;
            log.error("大模型调用异常! 耗时: {}ms, 异常信息: {}", costTime, e.getMessage(), e);
            log.info("========== 大模型调用结束 (异常) ==========");
            return null;
        }
    }

    /**
     * 从 AI 返回的文本中解析距离（公里）
     * 支持格式：约2,150-2,200公里、约2150公里、2200km、2,200 km 等
     */
    private BigDecimal parseDistanceFromAi(String aiText) {
        if (aiText == null || aiText.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            // 模式1: 匹配范围值，如 "2,150 - 2,200 公里" 或 "2150-2200公里" 或 "2,150~2,200"
            Pattern rangePattern = Pattern.compile(
                    "([\\d,]+(?:\\.\\d+)?)\\s*[-~至到]\\s*([\\d,]+(?:\\.\\d+)?)\\s*(?:公里|km|千米|KM|Km)",
                    Pattern.CASE_INSENSITIVE);
            Matcher rangeMatcher = rangePattern.matcher(aiText);
            if (rangeMatcher.find()) {
                double val1 = Double.parseDouble(rangeMatcher.group(1).replace(",", ""));
                double val2 = Double.parseDouble(rangeMatcher.group(2).replace(",", ""));
                BigDecimal avg = BigDecimal.valueOf((val1 + val2) / 2).setScale(1, java.math.RoundingMode.HALF_UP);
                log.info("从AI文本中解析到距离范围: {}-{} 公里, 取中间值: {}", val1, val2, avg);
                return avg;
            }

            // 模式2: 匹配单个值，如 "约2,150公里" 或 "2200 km"
            Pattern singlePattern = Pattern.compile(
                    "([\\d,]+(?:\\.\\d+)?)\\s*(?:公里|km|千米|KM|Km)",
                    Pattern.CASE_INSENSITIVE);
            Matcher singleMatcher = singlePattern.matcher(aiText);
            if (singleMatcher.find()) {
                double val = Double.parseDouble(singleMatcher.group(1).replace(",", ""));
                BigDecimal result = BigDecimal.valueOf(val).setScale(1, java.math.RoundingMode.HALF_UP);
                log.info("从AI文本中解析到距离: {} 公里", result);
                return result;
            }

            log.warn("未能从AI文本中解析出距离信息");
            return BigDecimal.ZERO;
        } catch (Exception e) {
            log.warn("解析AI距离信息异常: {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * 从 AI 返回的文本中解析预计时长（小时）
     * 支持格式：约24-28小时、约26小时、24~28h 等
     */
    private BigDecimal parseEstimatedHoursFromAi(String aiText) {
        if (aiText == null || aiText.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            // 模式1: 匹配范围值，如 "24-28 小时" 或 "24~28小时" 或 "24 - 28 h"
            Pattern rangePattern = Pattern.compile(
                    "([\\d,]+(?:\\.\\d+)?)\\s*[-~至到]\\s*([\\d,]+(?:\\.\\d+)?)\\s*(?:小时|个小时|h|hour|hours|H)",
                    Pattern.CASE_INSENSITIVE);
            Matcher rangeMatcher = rangePattern.matcher(aiText);
            if (rangeMatcher.find()) {
                double val1 = Double.parseDouble(rangeMatcher.group(1).replace(",", ""));
                double val2 = Double.parseDouble(rangeMatcher.group(2).replace(",", ""));
                BigDecimal avg = BigDecimal.valueOf((val1 + val2) / 2).setScale(1, java.math.RoundingMode.HALF_UP);
                log.info("从AI文本中解析到时长范围: {}-{} 小时, 取中间值: {}", val1, val2, avg);
                return avg;
            }

            // 模式2: 匹配单个值，如 "约26小时" 或 "26 h"
            Pattern singlePattern = Pattern.compile(
                    "([\\d,]+(?:\\.\\d+)?)\\s*(?:小时|个小时|h|hour|hours|H)",
                    Pattern.CASE_INSENSITIVE);
            Matcher singleMatcher = singlePattern.matcher(aiText);
            if (singleMatcher.find()) {
                double val = Double.parseDouble(singleMatcher.group(1).replace(",", ""));
                BigDecimal result = BigDecimal.valueOf(val).setScale(1, java.math.RoundingMode.HALF_UP);
                log.info("从AI文本中解析到时长: {} 小时", result);
                return result;
            }

            // 模式3: 匹配天数表述，如 "1-2天" 或 "约1.5天"，转换为小时
            Pattern dayRangePattern = Pattern.compile(
                    "([\\d,]+(?:\\.\\d+)?)\\s*[-~至到]\\s*([\\d,]+(?:\\.\\d+)?)\\s*(?:天|日)",
                    Pattern.CASE_INSENSITIVE);
            Matcher dayRangeMatcher = dayRangePattern.matcher(aiText);
            if (dayRangeMatcher.find()) {
                double val1 = Double.parseDouble(dayRangeMatcher.group(1).replace(",", "")) * 24;
                double val2 = Double.parseDouble(dayRangeMatcher.group(2).replace(",", "")) * 24;
                BigDecimal avg = BigDecimal.valueOf((val1 + val2) / 2).setScale(1, java.math.RoundingMode.HALF_UP);
                log.info("从AI文本中解析到天数范围, 转换为小时: {}", avg);
                return avg;
            }

            Pattern daySinglePattern = Pattern.compile(
                    "([\\d,]+(?:\\.\\d+)?)\\s*(?:天|日)");
            Matcher daySingleMatcher = daySinglePattern.matcher(aiText);
            if (daySingleMatcher.find()) {
                double val = Double.parseDouble(daySingleMatcher.group(1).replace(",", "")) * 24;
                BigDecimal result = BigDecimal.valueOf(val).setScale(1, java.math.RoundingMode.HALF_UP);
                log.info("从AI文本中解析到天数, 转换为小时: {}", result);
                return result;
            }

            log.warn("未能从AI文本中解析出时长信息");
            return BigDecimal.ZERO;
        } catch (Exception e) {
            log.warn("解析AI时长信息异常: {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public void autoDispatch(Long logisticsOrderId, String startAddress) {

        log.info("开始执行 AI 自动调度, 订单ID: {}", logisticsOrderId);
            LogisticsOrderDO order = logisticsOrderService.getLogisticsOrder(logisticsOrderId);
            if (order == null || order.getStatus() != LogisticsOrderStatusEnum.WAIT_DISPATCH.getStatus()) {
                throw new RuntimeException("订单不存在或状态不为待调度，跳过");
            }

            // 尝试从销售出库单中获取真实的仓库地址
            String realStartAddress = startAddress;
            if (order.getSaleOrderId() != null) {
                try {
                    List<cn.nova.erp.module.erp.dal.dataobject.sale.ErpSaleOutItemDO> outItems = saleOutService.getSaleOutItemListByOutId(order.getSaleOrderId());
                    if (CollUtil.isNotEmpty(outItems) && outItems.get(0).getWarehouseId() != null) {
                        cn.nova.erp.module.erp.dal.dataobject.stock.ErpWarehouseDO warehouse = warehouseService.getWarehouse(outItems.get(0).getWarehouseId());
                        if (warehouse != null) {
                            realStartAddress = cn.hutool.core.util.StrUtil.isNotBlank(warehouse.getAddress()) ? warehouse.getAddress() : warehouse.getName();
                        }
                    }
                } catch (Exception e) {
                    log.warn("获取发货仓库地址失败, 使用默认地址: {}", realStartAddress, e);
                }
            }

            // 1. 查询空闲车辆
            List<LogisticsVehicleDO> availableVehicles = logisticsVehicleService.getLogisticsVehicleList();
            availableVehicles.removeIf(v -> v.getStatus() == null || v.getStatus() != 0); // 0=空闲
            
            Long selectedVehicleId = null;
            
            if (CollUtil.isNotEmpty(availableVehicles)) {
                // 如果只有一个，直接分配
                if (availableVehicles.size() == 1) {
                    selectedVehicleId = availableVehicles.get(0).getId();
                } else {
                    // 让 AI 选择
                    String vehicleJson = JsonUtils.toJsonString(availableVehicles);
                    String promptText = String.format(
                            "你是一个专业的物流调度专家。这里有一个运输订单，货物信息为：%s。\n" +
                            "发货仓库地址为：%s。\n" +
                            "以下是当前可用的空闲车辆列表（JSON格式）：\n%s\n\n" +
                            "请根据货物的重量、体积等信息，选择一辆最合适的车辆。同时，请根据发货仓库地址，优先分配车牌归属地与仓库所在地相匹配的车辆。\n" +
                            "如果无法判断，请随机选择一辆。\n" +
                            "请只返回选中的车辆ID（纯数字，不要包含任何其他字符和符号）。",
                            order.getGoodsInfo(), realStartAddress, vehicleJson);
                            
                    // 调用第三方 API 选择车辆
                    String aiVehicleResult = callThirdPartyApi(promptText);
                    if (aiVehicleResult == null) {
                        aiVehicleResult = ""; // Fallback
                    }
                    log.info("第三方 API 选择的车辆ID: {}", aiVehicleResult);
                    
                    try {
                        // 提取数字
                        Matcher matcher = Pattern.compile("\\d+").matcher(aiVehicleResult);
                        if (matcher.find()) {
                            selectedVehicleId = Long.parseLong(matcher.group());
                            // 检查 ID 是否在列表中
                            final Long vId = selectedVehicleId;
                            boolean exists = availableVehicles.stream().anyMatch(v -> v.getId().equals(vId));
                            if (!exists) {
                                selectedVehicleId = availableVehicles.get(0).getId();
                            }
                        } else {
                            selectedVehicleId = availableVehicles.get(0).getId();
                        }
                    } catch (Exception e) {
                        selectedVehicleId = availableVehicles.get(0).getId();
                    }
                }
            } else {
                throw new RuntimeException("没有空闲车辆，无法自动调度");
            }
            
            LogisticsVehicleDO selectedVehicle = logisticsVehicleService.getLogisticsVehicle(selectedVehicleId);
            String vehicleInfo = selectedVehicle.getPlateNo() + " (载重" + selectedVehicle.getMaxWeight() + "吨)";

            // 2. 规划路线
            String routePromptText = String.format(
                    "你是一个专业的物流路线规划专家。请根据以下订单信息，规划一条最优配送路线，并给出合理的建议。\n" +
                            "【起点（发货地）】：%s\n" +
                            "【终点（收货地）】：%s\n" +
                            "【车辆信息】：%s\n" +
                            "【货物信息】：%s\n" +
                            "【时效要求】：无限制\n\n" +
                            "请输出以下内容（使用 Markdown 格式）：\n" +
                            "1. 路线建议（推荐的高速/省道，途经关键节点）\n" +
                            "2. 预计距离（公里估算）\n" +
                            "3. 预计时长（小时估算）\n" +
                            "4. 注意事项（天气、限行、货物保护等）",
                    realStartAddress != null ? realStartAddress : "仓库中心",
                    order.getReceiverAddress(),
                    vehicleInfo,
                    order.getGoodsInfo()
            );

            // 调用第三方 API 规划路线
            String aiSuggestion = callThirdPartyApi(routePromptText);
            if (aiSuggestion == null) {
                throw new RuntimeException("AI 路线规划失败，请人工介入处理。");
            }
            log.info("第三方 API 规划路线完成。");

            // 3. 从 AI 返回文本中解析距离和预计时长
            BigDecimal parsedDistance = parseDistanceFromAi(aiSuggestion);
            BigDecimal parsedHours = parseEstimatedHoursFromAi(aiSuggestion);
            log.info("AI 路线解析结果 => 距离: {} 公里, 预计时长: {} 小时", parsedDistance, parsedHours);

            // 4. 保存路线
            LogisticsRouteSaveReqVO routeReq = new LogisticsRouteSaveReqVO();
            routeReq.setName(order.getReceiverAddress() + " AI专线");
            routeReq.setStartAddress(realStartAddress != null ? realStartAddress : "仓库中心");
            routeReq.setEndAddress(order.getReceiverAddress());
            routeReq.setDistance(parsedDistance);
            routeReq.setEstimatedHours(parsedHours);
            routeReq.setStatus(0); // 0=可用
            routeReq.setRemark("AI自动规划路线");
            routeReq.setAiSuggestion(aiSuggestion);
            
            Long routeId = logisticsRouteService.createLogisticsRoute(routeReq);

            // 4. 更新订单和车辆状态
            logisticsVehicleService.updateVehicleStatus(selectedVehicleId, 1); // 1=运输中
            
            LogisticsOrderSaveReqVO updateOrder = new LogisticsOrderSaveReqVO();
            updateOrder.setId(logisticsOrderId);
            updateOrder.setVehicleId(selectedVehicleId);
            updateOrder.setRouteId(routeId);
            // 虽然有了车和路线，但是根据需求，如果要直接“发货”也可以。这里先设为待发货或运输中
            // 因为之前是如果当天出库，则待发货，有车有路线后，可以由人工点发货或者这里直接发货。
            // 需求说："运输订单状态设为待发货"
            updateOrder.setStatus(LogisticsOrderStatusEnum.WAIT_DISPATCH.getStatus()); 
            
            logisticsOrderService.updateLogisticsOrder(updateOrder);
            
            log.info("AI 自动调度完成，已分配车辆 {} 和路线 {}", selectedVehicleId, routeId);
    }

    @Async
    public void autoDispatchAsync(Long logisticsOrderId, String startAddress) {
        try {
            autoDispatch(logisticsOrderId, startAddress);
        } catch (Exception e) {
            log.error("异步 AI 自动调度发生异常", e);
        }
    }
}
