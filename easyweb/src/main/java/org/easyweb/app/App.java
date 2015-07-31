package org.easyweb.app;

import org.easyweb.util.DirectoryUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static String APP_NAME = "app.name";
    public static String APP_WEB_PATH = "app.web.path";
    public static String VELOCITY_NO_ESCAPE = "velocity.noescape";
    /**
     * app名称
     */
    private String name;

    /**
     * web目录
     */
    private List<String> webPaths;
    /**
     * 应用装修：
     * 0：部署中
     * 1：正常
     * 2：部署失败
     * -1：删除
     */
    private AppStatus status = AppStatus.INIT;

    /**
     * 应用部署运行时的文件目录，和deployVersion相关
     */
    private String rootPath;

    private String classpath;

    private Map<String, String> config;

    public App(String name) {
        this.name = name;
        this.config = new HashMap<String, String>();
    }

    public String getName() {
        return name;
    }

    public String getAppName() {
        return name;
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
        this.classpath = this.rootPath + "/classes";
    }

    public String getClasspath() {
        return classpath;
    }

    public List<String> getWebPaths() {
        return webPaths;
    }

    public void setWebPaths(List<String> webPaths) {
        this.webPaths = webPaths;
    }

    public AppStatus getStatus() {
        return status;
    }

    public void setStatus(AppStatus status) {
        this.status = status;
    }

    public String getConfig(String key) {
        return config.get(key);
    }

    public String getConfig(String key, String defaultValue) {
        String v = getConfig(key);
        return v != null ? v : defaultValue;
    }

    public void putConfig(String key, String value) {
        this.config.put(key, value);
    }
}
