package org.easyweb.app.monitor;

import org.easyweb.app.App;
import org.easyweb.app.AppStatus;

import java.io.File;
import java.util.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 上午11:42
 * <p/>
 * 每次扫描的结果
 */
public class ScanResult {

    /**
     * 扫描的app
     */
    private App app;

    private boolean restart;
    private boolean modified;
    /**
     * 所有的web文件
     */
    private Set<String> webGroovyFiles = new HashSet<String>();
    /**
     * 所有的biz文件
     */
    private Set<String> bizGroovyFiles = new HashSet<String>();
    /**
     * 变化的web groovy文件
     */
    private Set<String> modifiedWebGroovyFile = new HashSet<String>();


    private Map<String, List<String>> suffixFiles = new HashMap<String, List<String>>();

    public ScanResult(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public void setModifiedWebGroovyFile(Set<String> modifiedWebGroovyFile) {
        this.modifiedWebGroovyFile = modifiedWebGroovyFile;
    }

    public boolean isModified() {
        return modified || restart;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void setWebGroovyFiles(Set<String> webGroovyFiles) {
        this.webGroovyFiles = webGroovyFiles;
    }

    public void setBizGroovyFiles(Set<String> bizGroovyFiles) {
        this.bizGroovyFiles = bizGroovyFiles;
    }

    public Map<String, List<String>> getSuffixFiles() {
        return suffixFiles;
    }

    public void setSuffixFiles(Map<String, List<String>> suffixFiles) {
        this.suffixFiles = suffixFiles;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public boolean isRestart() {
        return restart;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public Set<String> getWebGroovyFiles() {
        return restart ? webGroovyFiles : modifiedWebGroovyFile;
    }

    public void addModifiedWebGroovyFile(String file) {
        this.webGroovyFiles.add(file);
        this.modifiedWebGroovyFile.add(file);
    }

    public Set<String> getBizGroovyFiles() {
        return bizGroovyFiles;
    }

    public void addBizGroovyFile(String file) {
        this.bizGroovyFiles.add(file);
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

    public ScanResult copyAndReset() {
        ScanResult result = new ScanResult(app);
        result.setBizGroovyFiles(this.bizGroovyFiles);
        result.setWebGroovyFiles(this.webGroovyFiles);
        result.setSuffixFiles(this.suffixFiles);
        result.setModifiedWebGroovyFile(new HashSet<String>(this.modifiedWebGroovyFile));
        result.setRestart(restart || app.getStatus() == AppStatus.INIT);
        result.setModified(modified);
        modifiedWebGroovyFile.clear();
        suffixFiles.clear();
        restart = false;
        modified = false;
        return result;
    }
}
