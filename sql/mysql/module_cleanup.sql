-- =================================================================================
-- Module Cleanup Script (MES, IoT, CRM, MP, Mall, Pay, BPM, Member) for ruoyi-vue-pro
-- Date: 2026-04-22
-- Description: 
-- 1. MES: Full Cleanup (Menu, Dicts, Tables)
-- 2. IoT: Full Cleanup (Menu, Dicts, Tables)
-- 3. CRM, MP, Mall, Pay, BPM, Member: Menu Only (Remove navigation and permissions)
-- =================================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 清理系统菜单 (system_menu)
-- ----------------------------

-- [A] 全量清理模块 (MES, IoT)
-- 删除 MES 菜单及其所有子菜单 (ID 5100 及以上)
DELETE FROM system_menu WHERE id >= 5100;
-- 删除 IoT 菜单及其子菜单 (ID 4000 到 5041)
DELETE FROM system_menu WHERE id BETWEEN 4000 AND 5041;

-- [B] 仅清理菜单模块 (CRM, MP, Mall, Pay, BPM, Member)
-- 根菜单 IDs:
-- 1117: 支付管理 (Pay)
-- 1185: 工作流程 (BPM)
-- 2084: 公众号管理 (MP)
-- 2262: 会员中心 (Member)
-- 2362: 商城系统 (Mall)
-- 2397: CRM 系统 (CRM)
-- 2563: ERP 系统 (注：用户未要求删除 ERP，保留)

DELETE FROM system_menu WHERE 
    -- 根菜单
    id IN (1117, 1185, 2084, 2262, 2362, 2397)
    -- 通过 parent_id 级联删除 (支持多层目录)
    OR parent_id IN (1117, 1185, 2084, 2262, 2362, 2397)
    OR parent_id IN (SELECT id FROM (SELECT id FROM system_menu WHERE parent_id IN (1117, 1185, 2084, 2262, 2362, 2397)) AS t)
    OR parent_id IN (SELECT id FROM (SELECT id FROM system_menu WHERE parent_id IN (SELECT id FROM (SELECT id FROM system_menu WHERE parent_id IN (1117, 1185, 2084, 2262, 2362, 2397)) AS t1)) AS t2)
    -- 通过权限标识补充删除 (确保按钮等也被清理)
    OR permission LIKE 'crm:%'
    OR permission LIKE 'mp:%'
    OR permission LIKE 'pay:%'
    OR permission LIKE 'bpm:%'
    OR permission LIKE 'member:%'
    OR permission LIKE 'mall:%'
    OR permission LIKE 'promotion:%'
    OR permission LIKE 'product:%'
    OR permission LIKE 'trade:%';

-- ----------------------------
-- 2. 清理权限关联 (system_role_menu)
-- ----------------------------
-- 删除所有已删除菜单的角色关联 (使用子查询清理孤儿记录)
DELETE FROM system_role_menu WHERE menu_id NOT IN (SELECT id FROM system_menu);

-- ----------------------------
-- 3. 清理字典定义 (system_dict_type)
-- ----------------------------
DELETE FROM system_dict_type WHERE type LIKE 'mes_%';
DELETE FROM system_dict_type WHERE type LIKE 'iot_%';

-- ----------------------------
-- 4. 清理字典数据 (system_dict_data)
-- ----------------------------
DELETE FROM system_dict_data WHERE dict_type LIKE 'mes_%';
DELETE FROM system_dict_data WHERE dict_type LIKE 'iot_%';

-- ----------------------------
-- 5. 删除 MES 业务表 (共 130+ 张表)
-- ----------------------------
DROP TABLE IF EXISTS mes_cal_holiday;
DROP TABLE IF EXISTS mes_cal_plan;
DROP TABLE IF EXISTS mes_cal_plan_shift;
DROP TABLE IF EXISTS mes_cal_plan_team;
DROP TABLE IF EXISTS mes_cal_team;
DROP TABLE IF EXISTS mes_cal_team_member;
DROP TABLE IF EXISTS mes_cal_team_shift;
DROP TABLE IF EXISTS mes_dv_check_plan;
DROP TABLE IF EXISTS mes_dv_check_plan_machinery;
DROP TABLE IF EXISTS mes_dv_check_plan_subject;
DROP TABLE IF EXISTS mes_dv_check_record;
DROP TABLE IF EXISTS mes_dv_check_record_line;
DROP TABLE IF EXISTS mes_dv_machinery;
DROP TABLE IF EXISTS mes_dv_machinery_type;
DROP TABLE IF EXISTS mes_dv_mainten_record;
DROP TABLE IF EXISTS mes_dv_mainten_record_line;
DROP TABLE IF EXISTS mes_dv_repair;
DROP TABLE IF EXISTS mes_dv_repair_line;
DROP TABLE IF EXISTS mes_dv_subject;
DROP TABLE IF EXISTS mes_md_auto_code_part;
DROP TABLE IF EXISTS mes_md_auto_code_record;
DROP TABLE IF EXISTS mes_md_auto_code_rule;
DROP TABLE IF EXISTS mes_md_client;
DROP TABLE IF EXISTS mes_md_item_batch_config;
DROP TABLE IF EXISTS mes_md_item;
DROP TABLE IF EXISTS mes_md_item_type;
DROP TABLE IF EXISTS mes_md_product_bom;
DROP TABLE IF EXISTS mes_md_product_sip;
DROP TABLE IF EXISTS mes_md_product_sop;
DROP TABLE IF EXISTS mes_md_unit_measure;
DROP TABLE IF EXISTS mes_md_vendor;
DROP TABLE IF EXISTS mes_md_workshop;
DROP TABLE IF EXISTS mes_md_workstation;
DROP TABLE IF EXISTS mes_md_workstation_machine;
DROP TABLE IF EXISTS mes_md_workstation_tool;
DROP TABLE IF EXISTS mes_md_workstation_worker;
DROP TABLE IF EXISTS mes_pro_andon_config;
DROP TABLE IF EXISTS mes_pro_andon_record;
DROP TABLE IF EXISTS mes_pro_card;
DROP TABLE IF EXISTS mes_pro_card_process;
DROP TABLE IF EXISTS mes_pro_feedback;
DROP TABLE IF EXISTS mes_pro_process_content;
DROP TABLE IF EXISTS mes_pro_process;
DROP TABLE IF EXISTS mes_pro_route;
DROP TABLE IF EXISTS mes_pro_route_process;
DROP TABLE IF EXISTS mes_pro_route_product_bom;
DROP TABLE IF EXISTS mes_pro_route_product;
DROP TABLE IF EXISTS mes_pro_task;
DROP TABLE IF EXISTS mes_pro_task_issue;
DROP TABLE IF EXISTS mes_pro_work_order_bom;
DROP TABLE IF EXISTS mes_pro_work_order;
DROP TABLE IF EXISTS mes_pro_work_record;
DROP TABLE IF EXISTS mes_pro_work_record_log;
DROP TABLE IF EXISTS mes_qc_defect;
DROP TABLE IF EXISTS mes_qc_defect_record;
DROP TABLE IF EXISTS mes_qc_indicator;
DROP TABLE IF EXISTS mes_qc_indicator_result_detail;
DROP TABLE IF EXISTS mes_qc_indicator_result;
DROP TABLE IF EXISTS mes_qc_ipqc;
DROP TABLE IF EXISTS mes_qc_ipqc_line;
DROP TABLE IF EXISTS mes_qc_iqc;
DROP TABLE IF EXISTS mes_qc_iqc_line;
DROP TABLE IF EXISTS mes_qc_oqc;
DROP TABLE IF EXISTS mes_qc_oqc_line;
DROP TABLE IF EXISTS mes_qc_rqc;
DROP TABLE IF EXISTS mes_qc_rqc_line;
DROP TABLE IF EXISTS mes_qc_template;
DROP TABLE IF EXISTS mes_qc_template_indicator;
DROP TABLE IF EXISTS mes_qc_template_item;
DROP TABLE IF EXISTS mes_tm_tool;
DROP TABLE IF EXISTS mes_tm_tool_type;
DROP TABLE IF EXISTS mes_wm_arrival_notice;
DROP TABLE IF EXISTS mes_wm_arrival_notice_line;
DROP TABLE IF EXISTS mes_wm_barcode_config;
DROP TABLE IF EXISTS mes_wm_barcode;
DROP TABLE IF EXISTS mes_wm_batch;
DROP TABLE IF EXISTS mes_wm_item_consume_detail;
DROP TABLE IF EXISTS mes_wm_item_consume;
DROP TABLE IF EXISTS mes_wm_item_consume_line;
DROP TABLE IF EXISTS mes_wm_item_receipt_detail;
DROP TABLE IF EXISTS mes_wm_item_receipt;
DROP TABLE IF EXISTS mes_wm_item_receipt_line;
DROP TABLE IF EXISTS mes_wm_material_stock;
DROP TABLE IF EXISTS mes_wm_misc_issue_detail;
DROP TABLE IF EXISTS mes_wm_misc_issue;
DROP TABLE IF EXISTS mes_wm_misc_issue_line;
DROP TABLE IF EXISTS mes_wm_misc_receipt_detail;
DROP TABLE IF EXISTS mes_wm_misc_receipt;
DROP TABLE IF EXISTS mes_wm_misc_receipt_line;
DROP TABLE IF EXISTS mes_wm_outsource_issue_detail;
DROP TABLE IF EXISTS mes_wm_outsource_issue;
DROP TABLE IF EXISTS mes_wm_outsource_issue_line;
DROP TABLE IF EXISTS mes_wm_outsource_receipt_detail;
DROP TABLE IF EXISTS mes_wm_outsource_receipt;
DROP TABLE IF EXISTS mes_wm_outsource_receipt_line;
DROP TABLE IF EXISTS mes_wm_package;
DROP TABLE IF EXISTS mes_wm_package_line;
DROP TABLE IF EXISTS mes_wm_product_issue_detail;
DROP TABLE IF EXISTS mes_wm_product_issue;
DROP TABLE IF EXISTS mes_wm_product_issue_line;
DROP TABLE IF EXISTS mes_wm_product_produce_detail;
DROP TABLE IF EXISTS mes_wm_product_produce;
DROP TABLE IF EXISTS mes_wm_product_produce_line;
DROP TABLE IF EXISTS mes_wm_product_receipt_detail;
DROP TABLE IF EXISTS mes_wm_product_receipt;
DROP TABLE IF EXISTS mes_wm_product_receipt_line;
DROP TABLE IF EXISTS mes_wm_product_sales_detail;
DROP TABLE IF EXISTS mes_wm_product_sales;
DROP TABLE IF EXISTS mes_wm_product_sales_line;
DROP TABLE IF EXISTS mes_wm_return_issue_detail;
DROP TABLE IF EXISTS mes_wm_return_issue;
DROP TABLE IF EXISTS mes_wm_return_issue_line;
DROP TABLE IF EXISTS mes_wm_return_sales_detail;
DROP TABLE IF EXISTS mes_wm_return_sales;
DROP TABLE IF EXISTS mes_wm_return_sales_line;
DROP TABLE IF EXISTS mes_wm_return_vendor_detail;
DROP TABLE IF EXISTS mes_wm_return_vendor;
DROP TABLE IF EXISTS mes_wm_return_vendor_line;
DROP TABLE IF EXISTS mes_wm_sales_notice;
DROP TABLE IF EXISTS mes_wm_sales_notice_line;
DROP TABLE IF EXISTS mes_wm_sn;
DROP TABLE IF EXISTS mes_wm_stock_taking_plan;
DROP TABLE IF EXISTS mes_wm_stock_taking_plan_param;
DROP TABLE IF EXISTS mes_wm_stock_taking_task;
DROP TABLE IF EXISTS mes_wm_stock_taking_task_line;
DROP TABLE IF EXISTS mes_wm_stock_taking_task_result;
DROP TABLE IF EXISTS mes_wm_transaction;
DROP TABLE IF EXISTS mes_wm_transfer_detail;
DROP TABLE IF EXISTS mes_wm_transfer;
DROP TABLE IF EXISTS mes_wm_transfer_line;
DROP TABLE IF EXISTS mes_wm_warehouse_area;
DROP TABLE IF EXISTS mes_wm_warehouse;
DROP TABLE IF EXISTS mes_wm_warehouse_location;

-- ----------------------------
-- 6. 删除 IoT 业务表 (共 15 张表)
-- ----------------------------
DROP TABLE IF EXISTS iot_alert_config;
DROP TABLE IF EXISTS iot_alert_record;
DROP TABLE IF EXISTS iot_device;
DROP TABLE IF EXISTS iot_device_group;
DROP TABLE IF EXISTS iot_device_modbus_config;
DROP TABLE IF EXISTS iot_device_modbus_point;
DROP TABLE IF EXISTS iot_ota_firmware;
DROP TABLE IF EXISTS iot_ota_task;
DROP TABLE IF EXISTS iot_ota_task_record;
DROP TABLE IF EXISTS iot_product_category;
DROP TABLE IF EXISTS iot_product;
DROP TABLE IF EXISTS iot_data_rule;
DROP TABLE IF EXISTS iot_data_sink;
DROP TABLE IF EXISTS iot_scene_rule;
DROP TABLE IF EXISTS iot_thing_model;

SET FOREIGN_KEY_CHECKS = 1;

-- 清理完成。建议重启应用并清理浏览器缓存。
