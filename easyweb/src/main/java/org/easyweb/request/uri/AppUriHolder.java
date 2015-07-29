package org.easyweb.request.uri;

import java.util.Map;

/**
 * User: jimmey/shantong
 * Date: 13-7-5
 * Time: 上午9:52
 */
public class AppUriHolder {

    private String appKey;

    private boolean restful;

    private Map<String, UriTemplate> uriTemplateMap;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public boolean isRestful() {
        return restful;
    }

    public void setRestful(boolean restful) {
        this.restful = restful;
    }

    public Map<String, UriTemplate> getUriTemplateMap() {
        return uriTemplateMap;
    }

    public void setUriTemplateMap(Map<String, UriTemplate> uriTemplateMap) {
        this.uriTemplateMap = uriTemplateMap;
    }
}
