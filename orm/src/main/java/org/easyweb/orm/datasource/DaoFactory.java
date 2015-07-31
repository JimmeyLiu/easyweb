package org.easyweb.orm.datasource;

import org.easyweb.orm.nutz.Slf4jAdapter;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.log.Logs;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jimmey/shantong
 * Date: 13-6-25
 * Time: 上午7:54
 */
public class DaoFactory {

    private static Map<DataSource, Dao> dsDaoMap = Collections.synchronizedMap(new HashMap<DataSource, Dao>());

    public static Dao getDao(DataSource dataSource) {
        Dao dao = dsDaoMap.get(dataSource);
        if (dao == null) {
            dao = new NutDao(dataSource);
            dsDaoMap.put(dataSource, dao);
        }
        return dao;
    }

}
