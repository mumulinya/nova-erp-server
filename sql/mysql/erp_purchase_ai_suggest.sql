CREATE TABLE erp_purchase_ai_suggest (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    product_id  BIGINT       NOT NULL COMMENT '产品ID',
    product_name VARCHAR(100) NOT NULL COMMENT '产品名称',
    current_stock INT        NOT NULL COMMENT '当前库存',
    safety_stock  INT        NOT NULL COMMENT '安全库存',
    suggest_count INT        NOT NULL COMMENT '建议采购数量',
    supplier_name VARCHAR(100) COMMENT '推荐供应商',
    suggest_time  DATE       COMMENT '建议采购时间',
    reason       TEXT        COMMENT 'AI分析原因',
    status       TINYINT     NOT NULL DEFAULT 0 COMMENT '0待确认 1已转订单 2已忽略',
    creator      VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater      VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted      BIT(1)      NOT NULL DEFAULT 0 COMMENT '是否删除',
    tenant_id    BIGINT      NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (id)
) COMMENT 'AI采购建议表';
