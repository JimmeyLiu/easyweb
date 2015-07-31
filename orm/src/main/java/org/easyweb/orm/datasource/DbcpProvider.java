package org.easyweb.orm.datasource;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

/**
 * Created by jimmey on 15-7-31.
 */
public class DbcpProvider implements DataSourceProvider {
    @Override
    public String type() {
        return "dbcp";
    }

    @Override
    public DataSource provide(String driverClass, String url, String username, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
