package org.easyweb.app;

import org.easyweb.util.DirectoryUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jimmey on 15-7-30.
 */
public class AppFileHelper {

    private static Map<String, AppFileType> map = new HashMap<String, AppFileType>();

    public static AppFileType getFileType(App app, File file) {
        AppFileType type = map.get(file.getAbsolutePath());
        if (type == null) {
            String suffix = getSuffix(file);
            boolean web = false;
            boolean groovy = "groovy".equals(suffix);
            boolean vm = "vm".equals(suffix);
            List<String> webPaths = app.getWebPaths();
            for (String p : webPaths) {
                web = file.getParent().startsWith(DirectoryUtil.getDirectory(app.getRootPath(), p));
                if (web) break;
            }
            if (web && groovy) {
                type = AppFileType.WEB_GROOVY;
            } else if (groovy) {
                type = AppFileType.BIZ_GROOVY;
            } else {
                type = AppFileType.OTHER;
            }
            map.put(file.getAbsolutePath(), type);
        }

        return type;
    }

    private static String getSuffix(File file) {
        String name = file.getName();
        int i = name.lastIndexOf(".");
        return name.substring(i + 1);
    }


}
