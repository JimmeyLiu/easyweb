package org.easyweb.orm;

import org.easyweb.orm.datasource.DaoFactory;
import org.easyweb.orm.datasource.TableChecker;
import org.easyweb.util.EasywebLogger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * User: jimmey/shantong
 * Date: 13-6-25
 * Time: 上午7:24
 */
public abstract class BaseDAO<T> {

    private Dao dao;

    public BaseDAO() {
    }

    public BaseDAO(DataSource dataSource) {
        this.dao = DaoFactory.getDao(dataSource);
    }

    public void init() {
        if (dao == null) {
            return;
        }
        try {
            new TableChecker(dao, getFirstGenericType()).run();
        } catch (SQLException e) {
            EasywebLogger.error("[TableChecker] table check error", e);
            throw new RuntimeException("表校验出错", e);
        }
    }

    public void insert(T t) {
        setValues(t);
        dao.insert(t);
    }

    public void update(T t) {
        setValues(t);
        dao.update(t);
    }

    public void updateIgnoreNull(T t) {
        setValues(t);
        dao.updateIgnoreNull(t);
    }

    public T findByStringKey(String id) {
        if (id == null) {
            return null;
        }
        return dao.fetch(getFirstGenericType(), id);
    }

    public T findByIntKey(Integer id) {
        if (id == null) {
            return null;
        }
        return dao.fetch(getFirstGenericType(), id);
    }

    public List<T> findAll() {
        return dao.query(getFirstGenericType(), null);
    }

    /**
     * 查询最新修改
     *
     * @param start
     * @return
     */
    public List<T> findNews(Date start) {
        return dao.query(getFirstGenericType(), Cnd.where("gmt_modified", ">", start));
    }

    public int countAll() {
        return dao.count(getFirstGenericType());
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    private Class<T> getFirstGenericType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String getTableName() {
        Class<?> clazz = getFirstGenericType();
        Table table = clazz.getAnnotation(Table.class);
        return table.value();
    }

    private void setValues(T t) {
        Class<?> clazz = getFirstGenericType();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(t);
                if ((field.getName().equals("gmtCreate") || field.getName().equals("gmtModified")) && value == null) {
                    field.set(t, new Date());
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
