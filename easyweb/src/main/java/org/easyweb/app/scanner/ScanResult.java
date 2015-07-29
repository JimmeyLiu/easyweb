package org.easyweb.app.scanner;

import org.easyweb.app.App;
import org.easyweb.app.App;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 上午11:42
 * <p/>
 * 每次扫描的结果
 */
public class ScanResult {

    /**
     * 开始扫描时间
     */
    private long startTime;
    /**
     * 扫描的app
     */
    private App app;
    private File appFile;
    /**
     * 所以的groovy文件
     */
    private List<String> groovyFiles = new ArrayList<String>();
    /**
     * 所有的web文件
     */
    private List<String> webGroovyFiles = new ArrayList<String>();
    /**
     * 所有的biz文件
     */
    private List<String> bizGroovyFiles = new ArrayList<String>();
    /**
     * 相对上次扫描发现的web变化文件
     */
    private List<String> modifiedWebGroovy = new ArrayList<String>();
    /**
     * 相对上次扫描发现的biz变化文件
     */
    private List<String> modifiedBizGroovy = new ArrayList<String>();
    private Map<String, List<String>> suffixFiles = new HashMap<String, List<String>>();
    private long useTime;

    public ScanResult(App app) {
        this.startTime = System.currentTimeMillis();
        this.app = app;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public List<String> getGroovyFiles() {
        return groovyFiles;
    }

    public void setGroovyFiles(List<String> groovyFiles) {
        this.groovyFiles = groovyFiles;
    }

    public List<String> getWebGroovyFiles() {
        return webGroovyFiles;
    }

//    public void setWebGroovyFiles(List<String> webGroovyFiles) {
//        this.webGroovyFiles = webGroovyFiles;
//    }

    public void addWebGroovyFile(String file) {
        this.webGroovyFiles.add(file);
        this.groovyFiles.add(file);
    }

    public List<String> getBizGroovyFiles() {
        return bizGroovyFiles;
    }

    public void addBizGroovyFile(String file) {
        this.bizGroovyFiles.add(file);
        this.groovyFiles.add(file);
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public void end() {
        this.useTime = System.currentTimeMillis() - startTime;
    }

    public File getAppFile() {
        return appFile;
    }

    public void setAppFile(File appFile) {
        this.appFile = appFile;
    }

    public List<String> getModifiedWebGroovy() {
        return modifiedWebGroovy;
    }

    public void addModifiedWebGroovy(String file) {
        this.modifiedWebGroovy.add(file);
    }

    public List<String> getModifiedBizGroovy() {
        return modifiedBizGroovy;
    }

    public void addModifiedBizGroovy(String file) {
        this.modifiedBizGroovy.add(file);
    }

    public List<String> getSuffixFiles(String suffix) {
        List<String> list = suffixFiles.get(suffix);
        if (list == null) {
            list = new ArrayList<String>();
            suffixFiles.put(suffix, list);
        }
        return list;
    }

    public void addSuffixFile(File file) {
        int i = file.getName().lastIndexOf(".");
        if (i > 0) {
            String suffix = file.getName().substring(i);
            getSuffixFiles(suffix).add(file.getAbsolutePath());
        }
    }
}
