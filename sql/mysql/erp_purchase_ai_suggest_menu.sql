INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show)
VALUES (2200, 'AI采购建议', 'erp:purchase:ai-suggest:query', 1, 99, 
        (SELECT id FROM (SELECT id FROM system_menu WHERE name = '采购管理') AS temp), 
        'ai-suggest', 'ep:cpu', 'erp/purchase/ai-suggest/index', 0, 1, 1, 1);

INSERT INTO system_menu (id, name, permission, type, sort, parent_id)
VALUES (2201, 'AI采购建议触发', 'erp:purchase:ai-suggest:generate', 3, 1, 2200);
INSERT INTO system_menu (id, name, permission, type, sort, parent_id)
VALUES (2202, 'AI采购建议确认', 'erp:purchase:ai-suggest:confirm', 3, 2, 2200);
INSERT INTO system_menu (id, name, permission, type, sort, parent_id)
VALUES (2203, 'AI采购建议忽略', 'erp:purchase:ai-suggest:ignore', 3, 3, 2200);
