package org.easyweb.app;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: jimmey/shantong
 * Date: 13-6-19
 * Time: 下午4:40
 */
public class AppParser {
    public static App parse(File file) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            App app = new App();
//            app.setIgnoreMethodType(Boolean.parseBoolean(properties.getProperty("ignore.method.type")));
            app.setName(properties.getProperty(App.APP_NAME));
            app.setVersion(properties.getProperty(App.APP_VERSION, "1.0"));
            String webPathStr = properties.getProperty(App.APP_WEB_PATH, "");
            List<String> webPaths = new ArrayList<String>();
            for (String v : webPathStr.split(",")) {
                if (StringUtils.isNotBlank(v)) {
                    webPaths.add(v);
                }
            }
            app.setWebPaths(webPaths);
            app.setRootPath(file);
            app.setProperties(properties);
            return app;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }

}
