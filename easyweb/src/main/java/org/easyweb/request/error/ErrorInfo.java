package org.easyweb.request.error;

import java.util.*;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午10:14
 */
public class ErrorInfo {

    private String errorType;
    /**
     * 错误类型下的默认信息
     */
    private String typeMessage;

    private Throwable throwable;

    private String exception;

    private String errorPageException;

    private String request;

    private Map<String, List<String>> deployInfos = new HashMap<String, List<String>>();

    private List<AppInfo> appInfos = new LinkedList<AppInfo>();

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getErrorPageException() {
        return errorPageException;
    }

    public void setErrorPageException(String errorPageException) {
        this.errorPageException = errorPageException;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Map<String, List<String>> getDeployInfos() {
        return deployInfos;
    }

    public void setDeployInfos(Map<String, List<String>> deployInfos) {
        this.deployInfos = deployInfos;
    }

    public void addDeployInfo(String type, String info) {
        List<String> infos = deployInfos.get(type);
        if (infos == null) {
            infos = new ArrayList<String>();
            deployInfos.put(type, infos);
        }
        infos.add(info);
    }

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    public void addAppInfo(AppInfo appInfo) {
        this.appInfos.add(appInfo);
    }
}
