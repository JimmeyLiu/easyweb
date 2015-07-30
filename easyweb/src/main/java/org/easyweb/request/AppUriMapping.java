package org.easyweb.request;

import org.easyweb.annocation.Page;
import org.easyweb.profiler.Profiler;
import org.easyweb.request.uri.UriTemplate;
import org.easyweb.app.App;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午3:05
 */
public class AppUriMapping {


    private static Map<String, Map<String, UriTemplate>> appUriMappings = new HashMap<String, Map<String, UriTemplate>>();

    /**
     * 性能上面会不会有问题。先这样，后续可以优化方向为：
     * 1、将为restful url单独放到map中，一个请求进来后先从map get一下，hash查找比每次循环效率还是要高的
     * 2、hash map查找不到后再循环restful url列表
     *
     * @param app
     * @param uri
     * @return
     */
    public static UriTemplate getUriTemplate(App app, String uri, String method) {
        try {
            Profiler.enter("get uri template");
            for (UriTemplate uriTemplate : getAppUris(app).values()) {
                if (uriTemplate.matches(uri) && method.equalsIgnoreCase(uriTemplate.getPageMethod().getHttpMethod())) {
                    return uriTemplate;
                }
            }
            return null;
        } finally {
            Profiler.release();
        }

    }

    public static void put(App app, Page page, File file, Method javaMethod) {
        String uri = page.url();
        PageMethod pageMethod = new PageMethod();
        pageMethod.setUrl(uri);
        pageMethod.setHttpMethod(page.method().name());
        pageMethod.setFile(file);
        pageMethod.setMethod(javaMethod);
        if (StringUtils.isNotBlank(page.layout())) {
            pageMethod.setLayout(page.layout());
        }
        UriTemplate uriTemplate = new UriTemplate(uri, pageMethod);
        if (uriTemplate.isRestful()) {

        }
        Map<String, UriTemplate> map = getAppUris(app);
        map.put(uri + "_" + page.method(), uriTemplate);
    }

    public static Map<String, UriTemplate> getAppUris(App app) {
        Map<String, UriTemplate> list = appUriMappings.get(app.getAppName());
        if (list == null) {
            list = new HashMap<String, UriTemplate>();
            appUriMappings.put(app.getAppName(), list);
        }
        return list;
    }

}
