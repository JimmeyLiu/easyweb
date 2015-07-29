package org.easyweb.request.debug;

import org.easyweb.context.ThreadContext;
import org.easyweb.request.error.AppInfo;
import org.easyweb.velocity.tool.SystemUtil;
import org.easyweb.app.App;
import org.easyweb.context.ThreadContext;
import org.easyweb.request.error.AppInfo;
import org.easyweb.velocity.tool.SystemUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午7:12
 */
public class DebugInfo {

    private String type;
    private App app;
    private List<Info> errorInfo = new LinkedList<Info>();
    private HttpServletRequest request;

    private List<Info> commonInfo = new LinkedList<Info>();
    private List<AppInfo> appInfos = new LinkedList<AppInfo>();

    private static String systemInfo;
    static {
        systemInfo = SystemUtil.getHostInfo().toString();
    }

    public DebugInfo(HttpServletRequest request) {
        this.app = ThreadContext.getContext().getApp();
        this.request = request;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public List<Info> getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(List<Info> errorInfo) {
        this.errorInfo = errorInfo;
    }

    public void addErrorInfo(Info info) {
        this.errorInfo.add(info);
    }

    public List<Info> getCommonInfo() {
        return commonInfo;
    }

    public void setCommonInfo(List<Info> commonInfo) {
        this.commonInfo = commonInfo;
    }

    public void addCommonInfo(Info info) {
        this.commonInfo.add(info);
    }

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    public static String getSystemInfo() {
        return systemInfo;
    }
}
