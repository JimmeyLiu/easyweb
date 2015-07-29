package org.easyweb.velocity.tool;

import org.easyweb.context.ThreadContext;
import org.easyweb.context.ThreadContext;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-22 下午1:40
 * <p/>
 * 提供页面信息的展示，如title、meta info、js（url或代码块）、css（url或代码块）
 */
public class PageAttributeTool {

    /**
     * 添加一段js代码片段到页头
     *
     * @param code
     */

    private final static String JS_1 = "<script charset=\"utf-8\" type=\"text/javascript\">\n";
    private final static String JS_2 = "<script charset=\"utf-8\" type=\"text/javascript\" src=\"";
    private final static String JS_1_END = "</script>\n";
    private final static String JS_2_END = "\"></script>\n";
    private final static String CSS_2 = "<link rel=\"stylesheet\" href=\"";
    private final static String CSS_2_END = "\" type=\"text/css\"/>\n";
    private final static String CSS_1 = "<style type=\"text/css\">\n";
    private final static String CSS_1_END = "</style>";
    private final static String JS_KEY = "_javaScript_head";
    private final static String JS_KEY_FOOT = "_javaScript_foot";
    private final static String CSS_KEY = "_styleSheet";
    private final static String TITLE_KEY = "_page_title";
    private final static String META_KEY = "_page_meta";

    /**
     * 在vm中使用 $assetsTool.addJs("/xxx.js")
     *
     * @param url
     */
    public static void addJs(String url) {
        addJs(url, "head");
    }

    public static void addJsCode(String code) {
        addJsCode(code, "head");
    }

    public static void addJs(String url, String section) {
        addJavascript(JS_2 + url + JS_2_END, section);
    }

    public static void addJsCode(String code, String section) {
        addJavascript(JS_1 + code + JS_1_END, section);
    }

    public static void setTitle(String title) {
        ThreadContext.getContext().putContext(TITLE_KEY, title);
    }

    public static String title(String defaultTitle) {
        String title = (String) ThreadContext.getContext().getContext(TITLE_KEY);
        if (title == null) {
            title = defaultTitle;
        }
        return title;
    }

    public static String title() {
        return title(null);
    }

    public static void addMeta(String name, String content) {
        Map<String, String> metas = (Map<String, String>) ThreadContext.getContext().getContext(META_KEY);
        if (metas == null) {
            metas = new LinkedHashMap<String, String>();
            ThreadContext.getContext().putContext(META_KEY, metas);
        }
        metas.put(name, content);
    }

    public static String metas() {
        Map<String, String> metas = (Map<String, String>) ThreadContext.getContext().getContext(META_KEY);
        if (metas == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : metas.entrySet()) {
            sb.append("<meta name=\"").append(entry.getKey()).append("\" content=\"").append(entry.getValue()).append("\"/>\n");
        }
        return sb.toString();
    }

    private static void addJavascript(String content, String section) {
        String key = JS_KEY;
        if ("foot".equals(section)) {
            key = JS_KEY_FOOT;
        }
        List<String> urls = (List<String>) ThreadContext.getContext().getContext(key);
        if (urls == null) {
            urls = new LinkedList<String>();
            ThreadContext.getContext().putContext(key, urls);
        }
        urls.add(content);
    }

    /**
     * 在vm中使用 $assetsTool.addCss("/xxx.css")
     *
     * @param url
     */
    public static void addCss(String url) {
        addStylesheet(CSS_2 + url + CSS_2_END);
    }

    public static void addCssCode(String code) {
        addStylesheet(CSS_1 + code + CSS_1_END);
    }

    private static void addStylesheet(String content) {
        List<String> urls = (List<String>) ThreadContext.getContext().getContext(CSS_KEY);
        if (urls == null) {
            urls = new LinkedList<String>();
            ThreadContext.getContext().putContext(CSS_KEY, urls);
        }
        urls.add(content);
    }

    /**
     * $assetsTool.js()
     *
     * @return
     */
    public static String js() {
        return js("head");
    }

    public static String js(String section) {
        String key = JS_KEY;
        if ("foot".equals(section)) {
            key = JS_KEY_FOOT;
        }
        List<String> urls = (List<String>) ThreadContext.getContext().getContext(key);
        if (urls == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String content : urls) {
            sb.append(content);
        }
        return sb.toString();
    }

    /**
     * $assetsTool.css()
     *
     * @return
     */
    public static String css() {
        List<String> urls = (List<String>) ThreadContext.getContext().getContext(CSS_KEY);
        if (urls == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(url);
        }
        return sb.toString();
    }
}
