package org.easyweb.orm.datasource;


import org.easyweb.app.App;
import org.easyweb.orm.nutz.Slf4jAdapter;
import org.nutz.log.Logs;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 下午2:15
 */
public class DataSourceFactory {

    static Map<String, DataSourceProvider> providerMap = new HashMap<String, DataSourceProvider>();

    static {
        ServiceLoader<DataSourceProvider> loader = ServiceLoader.load(DataSourceProvider.class);
        Iterator<DataSourceProvider> it = loader.iterator();
        while (it.hasNext()) {
            DataSourceProvider provider = it.next();
            providerMap.put(provider.type(), provider);
        }
        Logs.setAdapter(new Slf4jAdapter());
    }

    private static Map<String, DataSource> appDataSource = new ConcurrentHashMap<String, DataSource>(2);

    public static void register(App app, String dataSourceName, DataSource dataSource) {
        appDataSource.put(getAppDs(app.getName(), dataSourceName), dataSource);
    }

    public static DataSource getDataSource(String appKey, String name) {
        return appDataSource.get(getAppDs(appKey, name));
    }

    private static String getAppDs(String appKey, String name) {
        return appKey + "-" + name;
    }

    public static DataSourceProvider getProvider(String type) {
        return providerMap.get(type);
    }


}
