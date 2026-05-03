-- ----------------------------
-- 物流管理模块测试数据 (基于您最初确定的数据表结构)
-- ----------------------------

-- 1. 车辆管理表 (logistics_vehicle)
INSERT INTO `logistics_vehicle` (`id`, `plate_no`, `vehicle_type`, `driver_name`, `driver_mobile`, `max_weight`, `max_volume`, `status`, `remark`, `creator`, `updater`, `tenant_id`) VALUES 
(1, '粤B·12345', 1, '张师傅', '13800138000', 5.50, 15.00, 0, '冷藏车测试', 'admin', 'admin', 1),
(2, '粤B·88888', 2, '李师傅', '13900139000', 20.00, 45.00, 1, '重卡测试', 'admin', 'admin', 1),
(3, '京A·00001', 3, '王师傅', '13700137000', 2.50, 8.00, 0, '轻卡测试', 'admin', 'admin', 1);

-- 2. 配送路线表 (logistics_route)
INSERT INTO `logistics_route` (`id`, `name`, `start_address`, `end_address`, `distance`, `estimated_hours`, `status`, `remark`, `creator`, `updater`, `tenant_id`) VALUES 
(1, '深广干线', '深圳总仓', '广州分仓', 130.50, 2.50, 0, '高速路', 'admin', 'admin', 1),
(2, '深莞专线', '深圳总仓', '东莞集散中心', 60.00, 1.20, 0, '省道', 'admin', 'admin', 1),
(3, '广郊线路', '广州分仓', '广州市白云区', 25.00, 1.00, 0, '市内', 'admin', 'admin', 1);

-- 3. 运输订单表 (logistics_order)
INSERT INTO `logistics_order` (`id`, `no`, `sale_order_id`, `sale_order_no`, `receiver_name`, `receiver_mobile`, `receiver_address`, `goods_info`, `time_requirement`, `vehicle_id`, `route_id`, `status`, `remark`, `creator`, `updater`, `tenant_id`) VALUES 
(1, 'TR202405010001', 1001, 'SO202405010001', '陈先生', '13600136000', '广东省广州市天河区', '冷冻食品', '2024-05-10 12:00:00', 1, 1, 1, '冷链', 'admin', 'admin', 1),
(2, 'TR202405010002', 1002, 'SO202405010002', '林女士', '13500135000', '广东省东莞市松山湖', '普通零食', '2024-05-12 18:00:00', 2, 2, 0, '常温', 'admin', 'admin', 1);

-- 4. 运输费用表 (logistics_cost)
INSERT INTO `logistics_cost` (`id`, `logistics_order_id`, `freight_cost`, `extra_cost`, `total_cost`, `settlement_status`, `remark`, `creator`, `updater`, `tenant_id`) VALUES 
(1, 1, 1000.00, 500.00, 1500.00, 0, '过路费500', 'admin', 'admin', 1),
(2, 2, 800.00, 0.00, 800.00, 1, '已预付部分', 'admin', 'admin', 1);
