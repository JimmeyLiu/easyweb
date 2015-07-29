package org.easyweb.request.error;

import org.easyweb.app.App;
import org.easyweb.request.uri.UriTemplate;

import java.util.Map;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午11:55
 */
public class AppInfo {

    private App app;
    private Map<String, Object> beans;
    private Map<String, Object> vmTools;
    private Map<String, UriTemplate> appUris;

    public AppInfo(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Map<String, Object> getBeans() {
        return beans;
    }

    public void setBeans(Map<String, Object> beans) {
        this.beans = beans;
    }

    public Map<String, Object> getVmTools() {
        return vmTools;
    }

    public void setVmTools(Map<String, Object> vmTools) {
        this.vmTools = vmTools;
    }

    public Map<String, UriTemplate> getAppUris() {
        return appUris;
    }

    public void setAppUris(Map<String, UriTemplate> appUris) {
        this.appUris = appUris;
    }
}
