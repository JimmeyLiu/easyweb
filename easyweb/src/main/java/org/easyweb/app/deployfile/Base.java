package org.easyweb.app.deployfile;

import org.apache.commons.lang.StringUtils;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午1:22
 */
public class Base {

    public static String SPLIT = ",";

    String appName;
    /**
     * 在正式目录app的文件夹名称
     */
    String file;
    String md5;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public boolean containsAppKey(String line) {
        return !StringUtils.isBlank(line) && line.contains(appName);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(appName).append(",");
        sb.append(file).append(",");
        sb.append(md5);
        return sb.toString();
    }
}
