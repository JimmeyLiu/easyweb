package org.easyweb.app.scanner;

import org.easyweb.app.FileModified;
import org.easyweb.app.App;
import org.easyweb.util.EasywebLogger;
import org.easyweb.request.render.DirectoryUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 上午11:41
 * <p/>
 * app的页面扫描
 */
public class AppScanner {


    private static AppScanner instance;
    FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return (pathname.isDirectory() && !pathname.getName().startsWith(".") && !pathname.getName().equals("target")) || pathname.isFile();
        }
    };
    private Map<String, Long> appLastScan = new HashMap<String, Long>();

    private AppScanner() {
    }

    public static AppScanner getInstance() {
        if (instance == null) {
            instance = new AppScanner();
        }
        return instance;
    }

    public ScanResult scan(App app) {
        ScanResult result = new ScanResult(app);
        File appFile = new File(app.getRootPath());
        list(appFile, result, app, false);
        result.setAppFile(appFile);
        result.end();
        return result;
    }

    private void list(File file, ScanResult result, App app, boolean web) {
        if (file.isDirectory()) {
            if (!web) {
                for (String webPath : app.getWebPaths()) {
                    if (StringUtils.isBlank(webPath)) {
                        continue;
                    }
                    if (DirectoryUtil.getFilePath(file).endsWith(webPath)) {
                        web = true;
                        break;
                    }
                }
            }
            for (File c : file.listFiles(fileFilter)) {
                list(c, result, app, web);
            }
        } else {
            if (file.getName().endsWith(".groovy")) {
                boolean modified = FileModified.isModified(file);
                String path = file.getAbsolutePath();
                if (web) {
                    result.addWebGroovyFile(path);
                    if (modified) {
                        EasywebLogger.warn("web file %s modified", path);
                        result.addModifiedWebGroovy(path);
                    }

                } else {
                    result.addBizGroovyFile(path);
                    if (modified) {
                        EasywebLogger.warn("biz file %s modified", path);
                        result.addModifiedBizGroovy(path);
                    }
                }
            } else {
                result.addSuffixFile(file);
            }
        }
    }

}
