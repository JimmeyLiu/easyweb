package org.easyweb.orm.datasource;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.MappingField;
import org.nutz.dao.impl.jdbc.mysql.MysqlJdbcExpert;

import java.sql.SQLException;
import java.util.List;

/**
 * User: jimmey/shantong
 * Date: 13-6-25
 * Time: 上午8:04
 * <p/>
 * 表字段、表明
 */
public class TableChecker extends MysqlJdbcExpert {

    private Dao dao;

    private Class<?> clazz;

    public TableChecker(Dao dao, Class<?> clazz) {
        super(null);
        this.dao = dao;
        this.clazz = clazz;
    }

    public void run() throws SQLException {
        Entity entity = dao.getEntity(clazz);
        /**
         * 如果表不存在，则调用NutDao建表后直接返回
         */
        if (!dao.exists(clazz)) {
            dao.create(clazz, false);
            return;
        }
        List<MappingField> fields = entity.getMappingFields();
        for (MappingField field : fields) {
            if (exist(field, entity)) {
                continue;
            }
            addColumn(field, entity);
        }
        createIndexs(entity);
    }

    private boolean exist(MappingField field, Entity entity) {
        String sql = "SELECT " + field.getColumnName() + " FROM " + entity.getTableName() + " where 1!=1";
        try {
            dao.execute(Sqls.create(sql));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 参考SQLiteJdbcExpert代码
     *
     * @param field
     * @param entity
     * @throws java.sql.SQLException
     */
    private void addColumn(MappingField field, Entity entity) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ").append(entity.getTableName());
        sb.append(" add column ");
        sb.append(field.getColumnName());
        // Sqlite的整数型主键,一般都是自增的,必须定义为(PRIMARY KEY

        sb.append(' ').append(evalFieldType(field));
        if (field.isUnsigned()) {
            sb.append(" UNSIGNED");
        }
        if (field.isNotNull()) {
            sb.append(" NOT NULL");
        }
        if (field.hasDefaultValue()) {
            sb.append(" DEFAULT '").append(getDefaultValue(field)).append('\'');
        }
        sb.append(';');
        dao.execute(Sqls.create(sb.toString()));
    }

}
