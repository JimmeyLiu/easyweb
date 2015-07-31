package org.easyweb.orm.datasource;

import javax.sql.DataSource;

/**
 * Created by jimmey on 15-7-31.
 */
public interface DataSourceProvider {

    String type();

    DataSource provide(String driverClass, String url, String username, String password);

}
