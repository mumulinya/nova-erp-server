-- ==========================================================
-- ERP 模块全量互通测试数据脚本
-- 包含了：基础配置(单位/分类/仓库/账户) + 客户/供应商 + 商品 + 采购打单 + 销售打单 + 财务流水 + 最终库存
-- ==========================================================

-- ----------------------------
-- 1. 基础配置：仓库 (erp_warehouse)
-- ----------------------------
INSERT INTO `erp_warehouse`(`id`, `name`, `address`, `sort`, `principal`, `warehouse_price`, `freight_price`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, '默认主仓库', '浙江省杭州市余杭区文一西路88号', 1, '张三', 0.00, 0.00, 0, '主干仓库', '1', NOW(), '1', NOW(), 0, 1),
(2, '北京分仓', '北京市朝阳区', 2, '李四', 0.00, 0.00, 0, '', '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 2. 基础配置：商品单位 (erp_product_unit)
-- ----------------------------
INSERT INTO `erp_product_unit`(`id`, `name`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, '台', 0, '1', NOW(), '1', NOW(), 0, 1),
(2, '部', 0, '1', NOW(), '1', NOW(), 0, 1),
(3, '件', 0, '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 3. 基础配置：商品分类 (erp_product_category)
-- ----------------------------
INSERT INTO `erp_product_category`(`id`, `parent_id`, `name`, `code`, `sort`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 0, '数码家电', 'C001', 1, 0, '1', NOW(), '1', NOW(), 0, 1),
(2, 1, '智能手机', 'C001-1', 1, 0, '1', NOW(), '1', NOW(), 0, 1),
(3, 1, '笔记本电脑', 'C001-2', 2, 0, '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 4. 基础配置：结算账户 (erp_account)
-- ----------------------------
INSERT INTO `erp_account`(`id`, `name`, `no`, `remark`, `status`, `sort`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, '工商银行对公账户', '6222020202020202020', '主收款账户', 0, 1, 1, '1', NOW(), '1', NOW(), 0, 1),
(2, '微信支付商户', 'WX123456', '微信收款', 0, 2, 0, '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 5. 客户与供应商 (erp_customer, erp_supplier)
-- ----------------------------
INSERT INTO `erp_customer`(`id`, `name`, `contact`, `mobile`, `telephone`, `email`, `fax`, `remark`, `status`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, '杭州阿里巴巴网络科技有限公司', '马先生', '13888888888', '0571-88888888', 'ma@alibaba.com', '', '大客户', 0, 1, '1', NOW(), '1', NOW(), 0, 1),
(2, '北京字节跳动科技有限公司', '张先生', '13999999999', '', '', '', 'VIP客户', 0, 2, '1', NOW(), '1', NOW(), 0, 1);

INSERT INTO `erp_supplier`(`id`, `name`, `contact`, `mobile`, `telephone`, `email`, `fax`, `remark`, `status`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, '富士康科技集团', '郭先生', '13777777777', '', '', '', '主要组装供应商', 0, 1, '1', NOW(), '1', NOW(), 0, 1),
(2, '立讯精密', '王女士', '13666666666', '', '', '', '配件供应商', 0, 2, '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 6. 商品库 (erp_product)
-- ----------------------------
INSERT INTO `erp_product`(`id`, `category_id`, `unit_id`, `name`, `bar_code`, `status`, `weight`, `volume`, `price`, `min_price`, `max_price`, `cost_price`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 2, 2, 'iPhone 15 Pro 256G', '69011111111', 0, 0.20, 0.001, 7999.00, 7500.00, 8999.00, 6500.00, '热销手机', '1', NOW(), '1', NOW(), 0, 1),
(2, 3, 1, 'MacBook Pro 14寸 M3', '69022222222', 0, 1.60, 0.005, 12999.00, 12000.00, 14999.00, 10000.00, '办公设备', '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 7. 采购管理：采购订单 (假设我们向富士康采购了100台手机和50台电脑，已全部入库并完成付款)
-- ----------------------------
INSERT INTO `erp_purchase_order`(`id`, `no`, `supplier_id`, `order_time`, `total_count`, `total_price`, `discount_percent`, `discount_price`, `order_price`, `in_count`, `payment_price`, `account_id`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 'CG202604190001', 1, NOW(), 150.00, 1150000.00, 100.00, 0.00, 1150000.00, 150.00, 1150000.00, 1, 30, '首批备货打款完成', '1', NOW(), '1', NOW(), 0, 1);

INSERT INTO `erp_purchase_order_item`(`id`, `order_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `tax_percent`, `tax_price`, `in_count`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 1, 1, 2, 6500.00, 100.00, 650000.00, 0.00, 0.00, 100.00, '', '1', NOW(), '1', NOW(), 0, 1),
(2, 1, 2, 1, 10000.00, 50.00, 500000.00, 0.00, 0.00, 50.00, '', '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 8. 销售管理：销售订单 (假设阿里巴巴把主仓的手机买了20台，电脑买了10台，已出库且支付并欠些尾款)
-- ----------------------------
INSERT INTO `erp_sale_order`(`id`, `no`, `customer_id`, `order_time`, `total_count`, `total_price`, `discount_percent`, `discount_price`, `order_price`, `out_count`, `receipt_price`, `account_id`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 'XS202604190001', 1, NOW(), 30.00, 289970.00, 100.00, 0.00, 289970.00, 30.00, 200000.00, 1, 20, '阿里集中采购一批办公设备，尾款89970未付', '1', NOW(), '1', NOW(), 0, 1);

INSERT INTO `erp_sale_order_item`(`id`, `order_id`, `product_id`, `product_unit_id`, `product_price`, `count`, `total_price`, `tax_percent`, `tax_price`, `out_count`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 1, 1, 2, 7999.00, 20.00, 159980.00, 0.00, 0.00, 20.00, '给高管配置', '1', NOW(), '1', NOW(), 0, 1),
(2, 1, 2, 1, 12999.00, 10.00, 129990.00, 0.00, 0.00, 10.00, '给开发配置', '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 9. 财务管理：收款和付款单 (erp_finance_payment / erp_finance_receipt)
-- ----------------------------
-- 对于采购单的 付款单115万
INSERT INTO `erp_finance_payment`(`id`, `no`, `supplier_id`, `payment_time`, `account_id`, `total_price`, `discount_price`, `payment_price`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 'FK202604190001', 1, NOW(), 1, 1150000.00, 0.00, 1150000.00, 20, '支付富士康首批备货款', '1', NOW(), '1', NOW(), 0, 1);

-- 对于销售单的 收款单20万
INSERT INTO `erp_finance_receipt`(`id`, `no`, `customer_id`, `receipt_time`, `account_id`, `total_price`, `discount_price`, `receipt_price`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 'SK202604190001', 1, NOW(), 1, 289970.00, 0.00, 200000.00, 10, '阿里先付20万定金，剩下尾款作为应收留存', '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 10. 库存数据结果：库存表 (采购入了100部手机/50部电脑，销售出了20部手机/10部电脑。目前手机剩80部，电脑剩40部)
-- ----------------------------
INSERT INTO `erp_stock`(`id`, `product_id`, `warehouse_id`, `count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 1, 1, 80.00, '1', NOW(), '1', NOW(), 0, 1),
(2, 2, 1, 40.00, '1', NOW(), '1', NOW(), 0, 1);

-- ----------------------------
-- 11. 资金流水最终对账记录 (基于账户的变化)
-- ----------------------------
-- 主账户原本有0钱，支付115万，收款20万 (此处只做明细记录流水的展示，表不存在可以忽略)

