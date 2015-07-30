package org.easyweb.app;

import org.easyweb.util.DirectoryUtil;

import java.io.File;
import java.util.List;

public class App {

    public static String APP_NAME = "app.name";
    public static String APP_DOMAIN = "app.domain";
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
    private AppStatus status = AppStatus.INIT;

    /**
     * 应用部署运行时的文件目录，和deployVersion相关
     */
    private String rootPath;

    private String classpath;
    /**
     * 不做输出转码的配置
     */
    private String velocityNoEscape;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public String getVelocityNoEscape() {
        return velocityNoEscape;
    }

    public void setVelocityNoEscape(String velocityNoEscape) {
        this.velocityNoEscape = velocityNoEscape;
    }
}
