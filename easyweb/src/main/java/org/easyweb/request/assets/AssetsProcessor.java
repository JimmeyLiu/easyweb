package org.easyweb.request.assets;

import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.*;

/**
 * User: jimmey/shantong
 * Date: 13-8-2
 * Time: 下午7:27
 */
public class AssetsProcessor {

    public static Map<String, String> suffixType = new HashMap<String, String>();

    static {
        suffixType.put("js", "application/x-javascript");
        suffixType.put("css", "text/css;charset=" + Configuration.getRequestCharset());
        suffixType.put("jpg", "image/jpeg");
        suffixType.put("jpeg", "image/jpeg");
        suffixType.put("ico", "image/ico");
        suffixType.put("png", "image/png");
        suffixType.put("gif", "image/gif");
        suffixType.put("swf", "application/x-shockwave-flash");
    }

    public static boolean process(HttpServletRequest request, HttpServletResponse response, App app) throws Exception {
        if (app == null) {
            return false;
        }
        String suffix = getSuffix(request);
        boolean p = suffixType.containsKey(suffix);
        if (!p) {
            return false;
        }

        if (StringUtils.isNotBlank(request.getHeader("If-Modified-Since"))) {
            response.setHeader("Content-Length", "0");
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return true;
        }

        String uri = request.getRequestURI();// 这个key包含了appName信息在里面的
        /**
         * 支持 /sdk/assets/??a.css,b.css这种请求
         */
        String queryString = request.getQueryString();


        List<String> files = new ArrayList<String>();
        if (StringUtils.isNotBlank(queryString) && queryString.startsWith("?")) {
            queryString = URLDecoder.decode(queryString, "UTF-8");
            queryString = queryString.replaceAll("\r\n", "").replaceAll("\\s+", "");
            String paths[] = queryString.substring(1).split(",");
            for (String path : paths) {
                files.add(getFullPath(uri + path, app));
            }
        } else {
            files.add(getFullPath(uri, app));
        }
        long last = 0;
        int len = 0;
        List<byte[]> list = new ArrayList<byte[]>(files.size());
        for (String file : files) {
            AssetsData assetsData = StaticCache.get(file);
            if (assetsData == null) {
                continue;
            }
            byte[] bytes = assetsData.getBytes();
            list.add(bytes);
            len += bytes.length;
            last = assetsData.getLastModified() > last ? assetsData.getLastModified() : last;
        }

//        long lastModified = request.getDateHeader("If-Modified-Since");
//        final int CACHE_DURATION_IN_SECOND = 60 * 60 * 24 * 2; // 2 days
//        if (last <= lastModified) {
//            response.setHeader("Pragma", "publish");
//            response.addHeader("Cache-Control", "max-age=" + CACHE_DURATION_IN_SECOND);
//            response.addHeader("Cache-Control", "must-revalidate");//optional
//            response.setDateHeader("Last-Modified", last);
//            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
//        } else {
//        long expiry = new Date().getTime() + CACHE_DURATION_IN_SECOND * 1000;
//        response.setDateHeader("Expires", expiry);
        response.setHeader("Content-Length", len + "");
//        response.setDateHeader("Last-Modified", System.currentTimeMillis());
        response.setHeader("Content-Type", suffixType.get(suffix));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Pragma", "publish");
//        response.addHeader("Cache-Control", "max-age=" + CACHE_DURATION_IN_SECOND);
//        response.addHeader("Cache-Control", "must-revalidate");//optional
        for (byte[] bytes : list) {
            //增加换行后response中的Content-Length就不对了，导致浏览器下载的数据有问题
//            if(size > 1){//图片等不能增加字符
//                response.getOutputStream().write("\n\n\n".getBytes());
//            }
            response.getOutputStream().write(bytes);
        }
//        }

        return true;
    }

    private static String getFullPath(String uri, App app) {
        StringBuilder sb = new StringBuilder();
        int i = uri.indexOf(app.getName());
        sb.append("file:").append(app.getRootPath()).append(uri.substring(i + app.getName().length(), uri.length()));
        return sb.toString();
    }

    private static String getSuffix(HttpServletRequest request) {
        String uri = request.getRequestURI();// 这个key包含了appName信息在里面的
        int i = uri.lastIndexOf(".");
        if (i > 0) {
            return uri.substring(i + 1);
        } else if (uri.contains("/assets/") || uri.startsWith("/ewassets/")) {
            String queryString = request.getQueryString();
            String first = queryString.split(",")[0];
            i = first.lastIndexOf(".");
            return first.substring(i + 1);
        }
        /**
         * 支持 /sdk/assets/??a.css,b.css这种请求
         */

        return "";
    }

}
