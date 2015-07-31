package org.easyweb.app.monitor;

import org.easyweb.app.App;
import org.easyweb.util.DirectoryUtil;

import java.io.File;
import java.util.List;

/**
 * Created by jimmey on 15-7-30.
 */
public enum AppFileType {

    BIZ_GROOVY, WEB_GROOVY, OTHER;

    public static AppFileType getFileType(App app, File file) {
        String suffix = getSuffix(file);
        boolean web = false;
        boolean groovy = "groovy".equals(suffix);
        List<String> webPaths = app.getWebPaths();
        for (String p : webPaths) {
            web = file.getParent().startsWith(DirectoryUtil.getDirectory(app.getRootPath(), p));
            if (web) break;
        }
        AppFileType type = AppFileType.OTHER;
        if (web && groovy) {
            type = WEB_GROOVY;
        } else if (groovy) {
            type = BIZ_GROOVY;
        }
        return type;
    }

    private static String getSuffix(File file) {
        String name = file.getName();
        int i = name.lastIndexOf(".");
        return name.substring(i + 1);
    }
}
