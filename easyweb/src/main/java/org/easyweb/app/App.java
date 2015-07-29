package org.easyweb.app;

import org.easyweb.request.render.DirectoryUtil;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class App {

    public static String APP_NAME = "app.name";
    public static String APP_VERSION = "app.version";
    public static String APP_PARENT_VERSION = "app.parent.version";
    public static String APP_WEB_PATH = "app.web.path";
    /**
     * app名称
     */
    private String name;
    /**
     * app版本
     */
    private String version;
    /**
     * web目录
     */
    private List<String> webPaths;
    /**
     * app域名，可以不用
     */
    private String domain;
    /**
     * 应用装修：
     * 0：部署中
     * 1：正常
     * 2：部署失败
     * -1：删除
     */
    private int status = 0;

    /**
     * 应用部署运行时的文件目录，和deployVersion相关
     */
    private String rootPath;

    private Properties properties;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAppKey() {
        return name + "-" + version;
    }

    public String getRootPath() {
        return rootPath;
    }

    /**
     * 根目录是app.properties配置文件的父目录
     *
     * @param configFile
     */
    public void setRootPath(File configFile) {
        this.rootPath = DirectoryUtil.getFileParentPath(configFile);
    }

    public List<String> getWebPaths() {
        return webPaths;
    }

    public void setWebPaths(List<String> webPaths) {
        this.webPaths = webPaths;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
