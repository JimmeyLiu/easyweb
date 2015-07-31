package org.easyweb.orm.datasource;

import org.easyweb.util.EasywebLogger;
import org.nutz.dao.impl.SimpleDataSource;

import javax.sql.DataSource;

/**
 * Created by jimmey on 15-7-31.
 */
public class SimpleProvider implements DataSourceProvider {
    @Override
    public String type() {
        return "simple";
    }

    @Override
    public DataSource provide(String driverClass, String url, String username, String password) {
        SimpleDataSource dataSource = new SimpleDataSource();
        try {
            dataSource.setDriverClassName(driverClass);
        } catch (ClassNotFoundException e) {
            EasywebLogger.error(e);
            return null;
        }
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
