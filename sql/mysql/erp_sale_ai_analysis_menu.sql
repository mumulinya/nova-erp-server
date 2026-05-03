INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show)
VALUES (2220, 'AI销售分析', 'erp:sale:ai-analysis:query', 1, 99,
        (SELECT id FROM (SELECT id FROM system_menu WHERE name = '销售管理') AS temp),
        'ai-analysis', 'ep:trend-charts', 'erp/sale/ai-analysis/index', 0, 1, 1, 1);

INSERT INTO system_menu (id, name, permission, type, sort, parent_id)
VALUES (2221, 'AI销售分析触发', 'erp:sale:ai-analysis:generate', 3, 1, 2220);
