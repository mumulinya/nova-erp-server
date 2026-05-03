-- ==========================================================
-- ERP 模块全场景、全字段、多维度测试数据脚本 (Version 5.0)
-- 包含：基础配置、多级分类、阶梯价格商品、采购/销售/退货(复数项表)、库存变动(单数项表)、财务核销
-- 遵循：erp-2024-05-03.sql 与 Java DO 命名规范
-- ==========================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 基础配置：仓库 (erp_warehouse)
INSERT IGNORE INTO `erp_warehouse`(`id`, `name`, `address`, `sort`, `remark`, `principal`, `warehouse_price`, `truckage_price`, `status`, `default_status`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, '华东旗舰中转仓', '上海市浦东新区张江路88号', 10, '主要物流中心，支持全品类存储', '张大山', 5000.00, 200.00, 0, 1, '1', NOW(), 0, 1),
(11, '华南分拨中心', '广州市天河区中山大道北', 20, '负责大湾区配送，温控仓', '李广南', 3000.00, 150.00, 0, 0, '1', NOW(), 0, 1),
(12, '西北冷链仓库', '西安市未央区', 30, '特种货物存储', '周西北', 4500.00, 300.00, 0, 0, '1', NOW(), 0, 1);

-- 2. 基础配置：单位与分类
INSERT IGNORE INTO `erp_product_unit`(`id`, `name`, `status`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, '台', 0, '1', NOW(), 0, 1),
(11, '套', 0, '1', NOW(), 0, 1),
(12, '把', 0, '1', NOW(), 0, 1),
(13, '箱', 0, '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_product_category`(`id`, `parent_id`, `name`, `code`, `sort`, `status`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 0, '电子数码', 'DIGITAL', 10, 0, '1', NOW(), 0, 1),
(11, 10, '办公电脑', 'DIGITAL-PC', 1, 0, '1', NOW(), 0, 1),
(12, 10, '外设配件', 'DIGITAL-ACC', 2, 0, '1', NOW(), 0, 1),
(20, 0, '办公家具', 'FURNITURE', 20, 0, '1', NOW(), 0, 1);

-- 3. 基础配置：结算账户 (erp_account)
INSERT IGNORE INTO `erp_account`(`id`, `name`, `no`, `remark`, `status`, `sort`, `default_status`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, '招行基本户', '6225881001', '主营业务往来账号', 0, 1, 1, '1', NOW(), 0, 1),
(11, '支付宝对公', 'ALI-PAY-MAIN', '在线支付用', 0, 2, 0, '1', NOW(), 0, 1),
(12, '建行外币户', '722100099', '海外业务使用', 0, 3, 0, '1', NOW(), 0, 1);

-- 4. 往来单位：客户与供应商
INSERT IGNORE INTO `erp_customer`(`id`, `name`, `contact`, `mobile`, `email`, `remark`, `status`, `sort`, `tax_no`, `tax_percent`, `bank_name`, `bank_account`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, '腾讯云', '张大龙', '13100001111', '龍@tencent.com', '战略级客户', 0, 10, 'TX-999', 13.00, '招行深圳分行', '6222010', '1', NOW(), 0, 1),
(11, '美团', '王兴兴', '13888889999', '兴@meituan.com', '高频往来', 0, 11, 'MT-888', 13.00, '建行北京分行', '6217011', '1', NOW(), 0, 1),
(12, '字节跳动', '张小鸣', '13500002222', 'ming@bytedance.com', '新进潜力', 0, 12, 'BT-777', 13.00, '中行上海分行', '6228012', '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_supplier`(`id`, `name`, `contact`, `mobile`, `email`, `remark`, `status`, `sort`, `tax_no`, `tax_percent`, `bank_name`, `bank_account`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, '戴尔中国', 'DELL总', '400-DELL', 'china@dell.cn', '主要电脑商', 0, 10, 'DELL-TAX', 13.00, '花旗上海', '1234010', '1', NOW(), 0, 1),
(11, '三星半导体', 'SAM总', '021-SAM', 'ssd@samsung.com', '显示器/内存商', 0, 11, 'SAM-TAX', 13.00, '汇丰广州', '5678011', '1', NOW(), 0, 1),
(12, '罗技外设', '罗经理', '133001122', 'logi@link.com', '配件供应商', 0, 12, 'LOGI-TAX', 13.00, '交行合肥', '3344012', '1', NOW(), 0, 1);

-- 5. 商品资料 (erp_product)
INSERT IGNORE INTO `erp_product`(`id`, `name`, `bar_code`, `category_id`, `unit_id`, `status`, `standard`, `remark`, `expiry_day`, `weight`, `purchase_price`, `sale_price`, `min_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'Dell XPS 15', 'XPS15-PRO', 11, 10, 0, 'i9/64G/2T/RTX4080', '高端移动站', 1095, 2.10, 18000.00, 23888.00, 22000.00, '1', NOW(), 0, 1),
(11, '三星 OLED G9', 'SAM-G9-99', 12, 10, 0, '49寸/240Hz/曲面', '顶级电竞屏', 730, 15.00, 7500.00, 10999.00, 10500.00, '1', NOW(), 0, 1),
(12, '罗技 MX Master 3S', 'LOGI-MX3S', 12, 10, 0, '静音/8K DPI', '生产力神器', 365, 0.25, 450.00, 899.00, 850.00, '1', NOW(), 0, 1),
(20, 'HM Aeron Chair', 'HM-AERON-C', 20, 11, 0, 'C尺寸/波浪织物', '人体工学巅峰', 4380, 22.00, 9500.00, 14800.00, 14000.00, '1', NOW(), 0, 1);

-- 6. 采购链路 (PO -> PI -> Payment)
-- 6.1 采购订单 (erp_purchase_order & erp_purchase_order_items)
INSERT IGNORE INTO `erp_purchase_order`(`id`, `no`, `status`, `supplier_id`, `account_id`, `order_time`, `total_count`, `total_price`, `total_product_price`, `total_tax_price`, `discount_percent`, `discount_price`, `deposit_price`, `file_url`, `remark`, `in_count`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'PO-2026-XPS-001', 20, 10, 10, NOW(), 10.00, 180000.00, 180000.00, 0, 100.00, 0, 20000.00, 'http://oss/po/001.pdf', '戴尔XPS年度集采', 10.00, '1', NOW(), 0, 1),
(11, 'PO-2026-SAM-002', 20, 11, 10, NOW(), 5.00, 37500.00, 37500.00, 0, 100.00, 0, 0, '', '三星G9显示器补货', 5.00, '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_purchase_order_items`(`id`, `order_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `tax_percent`, `tax_price`, `remark`, `in_count`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 10, 10, 18000.00, 10.00, 180000.00, 0, 0, '需加急', 10.00, '1', NOW(), 0, 1),
(11, 11, 11, 10, 7500.00, 5.00, 37500.00, 0, 0, '', 5.00, '1', NOW(), 0, 1);

-- 6.2 采购入库 (erp_purchase_in & erp_purchase_in_items)
INSERT IGNORE INTO `erp_purchase_in`(`id`, `no`, `status`, `supplier_id`, `account_id`, `in_time`, `order_id`, `order_no`, `total_count`, `total_price`, `payment_price`, `total_product_price`, `total_tax_price`, `discount_percent`, `discount_price`, `other_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'PI-2026-XPS-001', 20, 10, 10, NOW(), 10, 'PO-2026-XPS-001', 10.00, 180000.00, 180000.00, 180000.00, 0, 100.00, 0, 0, '1', NOW(), 0, 1),
(11, 'PI-2026-SAM-001', 20, 11, 10, NOW(), 11, 'PO-2026-SAM-002', 5.00, 37500.00, 37500.00, 37500.00, 0, 100.00, 0, 50.00, '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_purchase_in_items`(`id`, `in_id`, `order_item_id`, `warehouse_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 10, 10, 10, 10, 18000.00, 10.00, 180000.00, '1', NOW(), 0, 1),
(11, 11, 11, 10, 11, 10, 7500.00, 5.00, 37500.00, '1', NOW(), 0, 1);

-- 7. 销售链路 (SO -> SOUT -> Receipt)
-- 7.1 销售订单 (erp_sale_order & erp_sale_order_items)
INSERT IGNORE INTO `erp_sale_order`(`id`, `no`, `status`, `customer_id`, `account_id`, `sale_user_id`, `order_time`, `total_count`, `total_price`, `total_product_price`, `total_tax_price`, `discount_percent`, `discount_price`, `deposit_price`, `out_count`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'SO-2026-TEN-001', 20, 10, 11, 1, NOW(), 5.00, 119440.00, 119440.00, 0, 100.00, 0, 10000.00, 5.00, '1', NOW(), 0, 1),
(11, 'SO-2026-MEI-002', 20, 11, 11, 1, NOW(), 10.00, 8990.00, 8990.00, 0, 100.00, 0, 0, 10.00, '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_sale_order_items`(`id`, `order_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `tax_percent`, `tax_price`, `out_count`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 10, 10, 23888.00, 5.00, 119440.00, 0, 0, 5.00, '1', NOW(), 0, 1),
(11, 11, 12, 10, 899.00, 10.00, 8990.00, 0, 0, 10.00, '1', NOW(), 0, 1);

-- 7.2 销售出库 (erp_sale_out & erp_sale_out_items)
INSERT IGNORE INTO `erp_sale_out`(`id`, `no`, `status`, `customer_id`, `account_id`, `sale_user_id`, `out_time`, `order_id`, `order_no`, `total_count`, `total_price`, `receipt_price`, `total_product_price`, `discount_percent`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'SOUT-2026-TEN-001', 20, 10, 11, 1, NOW(), 10, 'SO-2026-TEN-001', 5.00, 119440.00, 119440.00, 119440.00, 100.00, '1', NOW(), 0, 1),
(11, 'SOUT-2026-MEI-001', 20, 11, 11, 1, NOW(), 11, 'SO-2026-MEI-002', 10.00, 8990.00, 8990.00, 8990.00, 100.00, '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_sale_out_items`(`id`, `out_id`, `order_item_id`, `warehouse_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 10, 10, 10, 10, 23888.00, 5.00, 119440.00, '1', NOW(), 0, 1),
(11, 11, 11, 10, 12, 10, 899.00, 10.00, 8990.00, '1', NOW(), 0, 1);

-- 8. 其它库存变动 (单数项表: erp_stock_in_item, erp_stock_out_item, erp_stock_move_item)
-- 8.1 样品入库 (其它入库)
INSERT IGNORE INTO `erp_stock_in`(`id`, `no`, `supplier_id`, `in_time`, `total_count`, `total_price`, `status`, `remark`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'O-IN-2026-SAMPLE', 12, NOW(), 2.00, 900.00, 20, '罗技提供的新品样品', '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_stock_in_item`(`id`, `in_id`, `warehouse_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 11, 12, 13, 450.00, 2.00, 900.00, '1', NOW(), 0, 1);

-- 8.2 发送领用 (其它出库)
INSERT IGNORE INTO `erp_stock_out`(`id`, `no`, `customer_id`, `out_time`, `total_count`, `total_price`, `status`, `remark`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'O-OUT-2026-GIFT', 10, NOW(), 1.00, 14800.00, 20, '腾讯会议室赠送HM椅', '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_stock_out_item`(`id`, `out_id`, `warehouse_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 10, 20, 11, 14800.00, 1.00, 14800.00, '1', NOW(), 0, 1);

-- 8.3 库存调拨 (华东旗舰中转仓 -> 华南分拨中心)
INSERT IGNORE INTO `erp_stock_move`(`id`, `no`, `move_time`, `total_count`, `total_price`, `status`, `remark`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'MV-2026-XPS-TRANS', NOW(), 2.00, 36000.00, 20, '华南分部紧急申领两台XPS', '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_stock_move_item`(`id`, `move_id`, `from_warehouse_id`, `to_warehouse_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 10, 11, 10, 10, 18000.00, 2.00, 36000.00, '1', NOW(), 0, 1);

-- 9. 财务收支 (单数项表: erp_finance_payment_item, erp_finance_receipt_item)
-- 9.1 付款给戴尔 (核销采购单)
INSERT IGNORE INTO `erp_finance_payment`(`id`, `no`, `status`, `payment_time`, `finance_user_id`, `supplier_id`, `account_id`, `total_price`, `discount_price`, `payment_price`, `remark`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'FK-2026-DELL-01', 20, NOW(), 1, 10, 10, 180000.00, 1000.00, 179000.00, '戴尔货款清算', '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_finance_payment_item`(`id`, `payment_id`, `biz_type`, `biz_id`, `biz_no`, `total_price`, `paid_price`, `payment_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 11, 10, 'PI-2026-XPS-001', 180000.00, 0.00, 180000.00, '1', NOW(), 0, 1);

-- 9.2 收取腾讯款项 (核销销售单)
INSERT IGNORE INTO `erp_finance_receipt`(`id`, `no`, `status`, `receipt_time`, `finance_user_id`, `customer_id`, `account_id`, `total_price`, `discount_price`, `receipt_price`, `remark`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 'SK-2026-TEN-01', 20, NOW(), 1, 10, 11, 119440.00, 440.00, 119000.00, '腾讯云首批款项', '1', NOW(), 0, 1);

INSERT IGNORE INTO `erp_finance_receipt_item`(`id`, `receipt_id`, `biz_type`, `biz_id`, `biz_no`, `total_price`, `receipted_price`, `receipt_price`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 21, 10, 'SOUT-2026-TEN-001', 119440.00, 0.00, 119440.00, '1', NOW(), 0, 1);

-- 10. 结算库存状态 (erp_stock & erp_stock_record)
-- 商品10 (XPS) 上海仓余额: 入10 - 销5 - 拨2 = 3
INSERT IGNORE INTO `erp_stock`(`id`, `product_id`, `warehouse_id`, `count`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 10, 3.00, '1', NOW(), 0, 1),
-- XPS 广东仓余额: 拨入2
(11, 10, 11, 2.00, '1', NOW(), 0, 1),
-- 商品11 (三星G9) 上海仓余额: 入5
(12, 11, 10, 5.00, '1', NOW(), 0, 1);

-- 库存明细示例
INSERT IGNORE INTO `erp_stock_record`(`id`, `product_id`, `warehouse_id`, `count`, `total_count`, `biz_type`, `biz_id`, `biz_item_id`, `biz_no`, `creator`, `create_time`, `deleted`, `tenant_id`) VALUES
(10, 10, 10, 10.00, 10.00, 11, 10, 10, 'PI-2026-XPS-001', '1', NOW(), 0, 1),
(11, 10, 10, -5.00, 5.00, 21, 10, 10, 'SOUT-2026-TEN-001', '1', NOW(), 0, 1),
(12, 10, 10, -2.00, 3.00, 30, 10, 10, 'MV-2026-XPS-TRANS', '1', NOW(), 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
