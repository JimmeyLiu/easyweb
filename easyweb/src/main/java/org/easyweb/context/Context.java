package org.easyweb.context;

import org.easyweb.app.App;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class Context {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private App app;
    /**
     * 运行时组件的相对路径，在groovy中直接渲染velocity的时候用到
     */
    private String currentPath;
    private String layout;
    /**
     * 外部重定向地址，即302
     */
    private String redirectTo;
    /**
     * 内部重定向地址，不做302跳转
     */
    private String forwardTo;
    private boolean isDownload;
    private Map<String, Object> contextMap = new HashMap<String, Object>();

    private boolean breakPipeline;

    public Object getContext(String key) {
        return contextMap.get(key);
    }

    public String getAppName() {
        if (app == null) {
            return "easyweb";
        }
        return app.getAppName();
    }

    public Map<String, Object> getContextMap() {
        return contextMap;
    }

    public void setContextMap(Map<String, Object> contextMap) {
        this.contextMap = contextMap;
    }

    public void putContext(String key, Object value) {
        this.contextMap.put(key, value);
    }

    public void putAll(Map<String, Object> contextMap) {
        this.contextMap.putAll(contextMap);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        putContext("request", request);
        this.request = request;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public boolean isBreakPipeline() {
        return breakPipeline;
    }

    public void setBreakPipeline(boolean breakPipeline) {
        this.breakPipeline = breakPipeline;
    }

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }
}
