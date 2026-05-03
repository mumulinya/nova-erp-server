-- 1. 运输订单表
CREATE TABLE `logistics_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '运输订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '运输单号',
  `sale_order_id` bigint NOT NULL COMMENT '关联销售订单ID',
  `sale_order_no` varchar(64) NOT NULL COMMENT '销售订单号',
  `receiver_name` varchar(64) NOT NULL COMMENT '收货人姓名',
  `receiver_mobile` varchar(20) NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(255) NOT NULL COMMENT '收货地址',
  `goods_info` varchar(500) NOT NULL COMMENT '货物信息',
  `time_requirement` varchar(100) DEFAULT NULL COMMENT '时效要求',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '运输状态（0待发货 1运输中 2已签收）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运输订单表';

-- 2. 车辆管理表
CREATE TABLE `logistics_vehicle` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
  `plate_no` varchar(32) NOT NULL COMMENT '车牌号',
  `vehicle_type` varchar(64) NOT NULL COMMENT '车辆类型',
  `load_capacity` decimal(10,2) NOT NULL COMMENT '载重(吨)',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '当前状态（0空闲 1运输中 2维修中）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆管理表';

-- 3. 配送路线表
CREATE TABLE `logistics_route` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '路线ID',
  `name` varchar(100) NOT NULL COMMENT '路线名称',
  `start_address` varchar(255) NOT NULL COMMENT '起点地址',
  `end_address` varchar(255) NOT NULL COMMENT '终点地址',
  `distance` decimal(10,2) NOT NULL COMMENT '路线距离(km)',
  `estimated_hours` decimal(10,2) NOT NULL COMMENT '预计时长(小时)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `ai_suggestion` text COMMENT 'AI路线建议',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配送路线表';

-- 4. 运输费用表
CREATE TABLE `logistics_cost` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '费用单ID',
  `logistics_order_id` bigint NOT NULL COMMENT '运输单ID',
  `transport_cost` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '运输成本(元)',
  `fuel_cost` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '燃油费(元)',
  `toll_cost` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '过路费(元)',
  `other_cost` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '其他费用(元)',
  `total_cost` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '合计(元)',
  `settlement_status` tinyint NOT NULL DEFAULT '0' COMMENT '费用结算状态（0未结算 1部分结算 2已结算）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运输费用表';

-- ============================================
-- 如果表已存在，使用以下 ALTER 语句增加关联字段
-- ============================================
-- ALTER TABLE `logistics_order` ADD COLUMN `sale_order_no` varchar(64) NOT NULL COMMENT '销售订单号' AFTER `sale_order_id`;
-- ALTER TABLE `logistics_order` ADD COLUMN `receiver_name` varchar(64) NOT NULL COMMENT '收货人姓名' AFTER `sale_order_no`;
-- ALTER TABLE `logistics_order` ADD COLUMN `receiver_mobile` varchar(20) NOT NULL COMMENT '收货人电话' AFTER `receiver_name`;
-- ALTER TABLE `logistics_order` ADD COLUMN `vehicle_id` bigint DEFAULT NULL COMMENT '关联车辆ID' AFTER `receiver_mobile`;
-- ALTER TABLE `logistics_order` ADD COLUMN `route_id` bigint DEFAULT NULL COMMENT '关联配送路线ID' AFTER `vehicle_id`;
-- ALTER TABLE `logistics_order` ADD COLUMN `out_time` datetime DEFAULT NULL COMMENT '出库时间' AFTER `route_id`;
