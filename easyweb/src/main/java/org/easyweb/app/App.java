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
    public static String GROOVY_METHOD_INTERCEPTOR = "groovy.method.interceptor";
    public static String PROFILER_THRESHOLD = "profiler.threshold";

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

    private String rootParent;
    /**
     * 应用部署运行时的文件目录，和deployVersion相关
     */
    private String rootPath;

    private String classpath;

    private Map<String, String> config;

    private boolean groovyMethodInterceptor;

    private int profilerThreshold;

    public App(String name) {
        this.name = name;
        this.config = new HashMap<String, String>();
    }

    public String getName() {
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
        this.rootParent = DirectoryUtil.getFileParentPath(configFile.getParentFile());
        this.classpath = this.rootPath + "/classes";
    }

    /**
     * 在app父目录增加 name.lock的文件来锁定app，当这个文件存在的时候，发布线程将忽略目录下面的修改。
     * <p/>
     * 用于app线上发布，流程如下：
     * 1. 线上app打包发布时，将app打出zip包；
     * 2. 发布时，先创建demo.lock文件
     * 3. 再将app下线；
     * 4. 删除老的代码
     * 5. 将新的代码包解压到目录下
     * 6. 删除demo.lock
     * <p/>
     * 以上操作通过发布脚本来实现
     *
     * @return
     */
    public File getLock() {
        return new File(rootParent, name + ".lock");
    }

    public String getRootParent() {
        return rootParent;
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

    public boolean isGroovyMethodInterceptor() {
        return groovyMethodInterceptor;
    }

    public void setGroovyMethodInterceptor(boolean groovyMethodInterceptor) {
        this.groovyMethodInterceptor = groovyMethodInterceptor;
    }

    public int getProfilerThreshold() {
        return profilerThreshold;
    }

    public void setProfilerThreshold(int profilerThreshold) {
        this.profilerThreshold = profilerThreshold;
    }

    public void putConfig(String key, String value) {
        this.config.put(key, value);
    }
}
