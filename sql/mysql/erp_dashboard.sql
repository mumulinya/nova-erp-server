-- 供应链大屏 & 库存预警：为产品表增加安全库存字段
ALTER TABLE erp_product ADD COLUMN safe_stock decimal(24,6) DEFAULT 10.000000 COMMENT '安全库存数量';
