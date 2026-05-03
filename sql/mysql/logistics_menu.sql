-- 1. 目录：物流管理 (ID: 2000)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2000, '物流管理', '', 1, 10, 0, '/logistics', 'ep:van', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 2. 菜单：运输订单管理 (ID: 2001)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2001, '运输订单管理', '', 2, 1, 2000, 'order', 'ep:tickets', 'erp/logistics/order/index', 'LogisticsOrder', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮：运输订单管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2002, '查询运输订单', 'erp:logistics-order:query', 3, 1, 2001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2003, '新增运输订单', 'erp:logistics-order:create', 3, 2, 2001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2004, '修改运输订单', 'erp:logistics-order:update', 3, 3, 2001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2005, '删除运输订单', 'erp:logistics-order:delete', 3, 4, 2001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2006, '导出运输订单', 'erp:logistics-order:export', 3, 5, 2001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 3. 菜单：车辆管理 (ID: 2010)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2010, '车辆管理', '', 2, 2, 2000, 'vehicle', 'ep:truck', 'erp/logistics/vehicle/index', 'LogisticsVehicle', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮：车辆管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2011, '查询车辆', 'erp:logistics-vehicle:query', 3, 1, 2010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2012, '新增车辆', 'erp:logistics-vehicle:create', 3, 2, 2010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2013, '修改车辆', 'erp:logistics-vehicle:update', 3, 3, 2010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2014, '删除车辆', 'erp:logistics-vehicle:delete', 3, 4, 2010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2015, '导出车辆', 'erp:logistics-vehicle:export', 3, 5, 2010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 4. 菜单：配送路线管理 (ID: 2020)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2020, '配送路线管理', '', 2, 3, 2000, 'route', 'ep:map-location', 'erp/logistics/route/index', 'LogisticsRoute', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮：配送路线管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2021, '查询配送路线', 'erp:logistics-route:query', 3, 1, 2020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2022, '新增配送路线', 'erp:logistics-route:create', 3, 2, 2020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2023, '修改配送路线', 'erp:logistics-route:update', 3, 3, 2020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2024, '删除配送路线', 'erp:logistics-route:delete', 3, 4, 2020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2025, '导出配送路线', 'erp:logistics-route:export', 3, 5, 2020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 5. 菜单：运输费用管理 (ID: 2030)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2030, '运输费用管理', '', 2, 4, 2000, 'cost', 'ep:money', 'erp/logistics/cost/index', 'LogisticsCost', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮：运输费用管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES 
(2031, '查询运输费用', 'erp:logistics-cost:query', 3, 1, 2030, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2032, '新增运输费用', 'erp:logistics-cost:create', 3, 2, 2030, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2033, '修改运输费用', 'erp:logistics-cost:update', 3, 3, 2030, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2034, '删除运输费用', 'erp:logistics-cost:delete', 3, 4, 2030, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(2035, '导出运输费用', 'erp:logistics-cost:export', 3, 5, 2030, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
