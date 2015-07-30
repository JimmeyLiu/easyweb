package org.easyweb.app;

import org.easyweb.util.DirectoryUtil;

import java.io.File;
import java.util.List;

/**
 * Created by jimmey on 15-7-30.
 */
public class AppFileHelper {

    public static AppFileType getFileType(App app, File file) {
        String suffix = getSuffix(file);
        boolean web = false;
        boolean groovy = "groovy".equals(suffix);
        boolean vm = "vm".equals(suffix);
        List<String> webPaths = app.getWebPaths();
        for (String p : webPaths) {
            web = file.getParent().startsWith(DirectoryUtil.getAppDeployFile(app.getAppName(), p));
            if (web) break;
        }
        if (web) {
            if (groovy) return AppFileType.WEB_GROOVY;
            if (vm) return AppFileType.WEB_VM;
        } else if (groovy) {
            return AppFileType.BIZ_GROOVY;
        }
        return AppFileType.OTHER;
    }

    public static String getSuffix(File file) {
        String name = file.getName();
        int i = name.lastIndexOf(".");
        return name.substring(i + 1);
    }


}
