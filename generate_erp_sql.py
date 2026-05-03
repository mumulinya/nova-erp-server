import os
import re

dir_path = r'D:\erp-proect\ruoyi-vue-pro\yudao-module-erp\src\main\java\cn\iocoder\yudao\module\erp\dal\dataobject'
sql_statements = []

for root, _, files in os.walk(dir_path):
    for file in files:
        if file.endswith('DO.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()

                # Get table name
                table_match = re.search(r'@TableName\("([^"]+)"\)', content)
                table_name = table_match.group(1) if table_match else file.replace('DO.java', '').lower()

                # Get table comment
                class_comment_match = re.search(r'/\*\*.*?\n\s*\*?\s*(.*?)\n', content, re.DOTALL)
                table_comment = class_comment_match.group(1).strip() if class_comment_match else 'ERP Table'
                if table_comment.startswith('*'): table_comment = table_comment[1:].strip()

                sql = 'CREATE TABLE `' + table_name + '` (\n'

                fields = re.findall(r'(?:/\*\*.*?([^\n\*]+)\s*\*?\n\s*\*/\n\s*)?(?:@[^\n]+\n\s*)*(?:private|protected)\s+([A-Z][a-zA-Z<>]+)\s+([a-zA-Z0-9_]+)', content, re.DOTALL)

                pk = ''
                for comment, field_type, field_name in fields:
                    if field_name == 'serialVersionUID': continue
                    db_field = re.sub(r'(?<!^)(?=[A-Z])', '_', field_name).lower()

                    if 'Long' in field_type: db_type = 'BIGINT'
                    elif 'Integer' in field_type: db_type = 'INT'
                    elif 'BigDecimal' in field_type: db_type = 'DECIMAL(24, 6)'
                    elif 'Boolean' in field_type: db_type = 'BIT(1)'
                    elif 'LocalDateTime' in field_type: db_type = 'DATETIME'
                    elif 'String' in field_type: db_type = 'VARCHAR(255)'
                    else: db_type = 'VARCHAR(255)' # fallback

                    is_pk = ('id' == db_field)
                    if is_pk:
                        pk = db_field
                        db_type += ' AUTO_INCREMENT'

                    comment_str = ' COMMENT ' + '"' + (comment.strip() if comment else '') + '"'
                    sql += '  `' + db_field + '` ' + db_type + comment_str + ',\n'

                sql += '  `creator` varchar(64) DEFAULT \'\' COMMENT \'创建者\',\n  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\n  `updater` varchar(64) DEFAULT \'\' COMMENT \'更新者\',\n  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\',\n  `deleted` bit(1) NOT NULL DEFAULT b\'0\' COMMENT \'是否删除\',\n  `tenant_id` bigint NOT NULL DEFAULT \'0\' COMMENT \'租户编号\',\n'

                if pk:
                    sql += '  PRIMARY KEY (`' + pk + '`)\n'
                else:
                    sql = sql[:-2] + '\n'

                sql += ') COMMENT=' + '"' + table_comment + '"' + ';\n'
                sql_statements.append(sql)

out_file = r'D:\erp-proect\ruoyi-vue-pro\sql\mysql\erp_tables.sql'
with open(out_file, 'w', encoding='utf-8') as f:
    f.write('\n'.join(sql_statements))
print('Generated SQL file at ' + out_file)
