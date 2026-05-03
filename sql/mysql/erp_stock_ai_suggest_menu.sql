INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show)
VALUES (2210, 'AI库存优化', 'erp:stock:ai-suggest:query', 1, 99,
        (SELECT id FROM (SELECT id FROM system_menu WHERE name = '库存管理') AS temp),
        'ai-suggest', 'ep:cpu', 'erp/stock/ai-suggest/index', 0, 1, 1, 1);

INSERT INTO system_menu (id, name, permission, type, sort, parent_id)
VALUES (2211, 'AI库存分析触发', 'erp:stock:ai-suggest:generate', 3, 1, 2210);
INSERT INTO system_menu (id, name, permission, type, sort, parent_id)
VALUES (2212, 'AI库存建议处理', 'erp:stock:ai-suggest:handle', 3, 2, 2210);
INSERT INTO system_menu (id, name, permission, type, sort, parent_id)
VALUES (2213, 'AI库存建议忽略', 'erp:stock:ai-suggest:ignore', 3, 3, 2210);
