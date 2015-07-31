package org.easyweb.orm.datasource;

import org.apache.commons.lang.StringUtils;
import org.easyweb.app.App;
import org.easyweb.app.deploy.DeployException;
import org.easyweb.app.deploy.DeployPhase;
import org.easyweb.app.deploy.Deployer;
import org.easyweb.app.deploy.process.FileProcessor;
import org.easyweb.app.monitor.ScanResult;

import javax.sql.DataSource;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 下午5:23
 */
@Deployer(DeployPhase.COMPILE_GROOVY)
public class DataSourceProcessor extends FileProcessor {

    private static String DS_NAME = "ds.name";
    private static String DS_TYPE = "ds.%s.type";
    private static String DS_DRIVER = "ds.%s.driver";
    private static String DS_URL = "ds.%s.url";
    private static String DS_USERNAME = "ds.%s.username";
    private static String DS_PASSWORD = "ds.%s.password";

    @Override
    public void process(ScanResult result) throws DeployException {
        App app = result.getApp();
        String names = app.getConfig(DS_NAME);
        if (names == null) {
            return;
        }

        for (String name : names.split(",")) {
            name = name.trim();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            String type = app.getConfig(String.format(DS_TYPE, name));
            DataSourceProvider provider = DataSourceFactory.getProvider(type);
            if (provider == null) {
                throw new DeployException("DataSource Provider" + type + "Not Found");
            }
            String driverClassName = app.getConfig(String.format(DS_DRIVER, name));
            String url = app.getConfig(String.format(DS_URL, name));
            String username = app.getConfig(String.format(DS_USERNAME, name), "");
            String password = app.getConfig(String.format(DS_PASSWORD, name), "");

            DataSource dataSource = provider.provide(driverClassName, url, username, password);
            if (dataSource == null) {
                throw new DeployException("DataSource " + name + " Provide Error");
            }
            DataSourceFactory.register(app, name, dataSource);
        }


    }
}
