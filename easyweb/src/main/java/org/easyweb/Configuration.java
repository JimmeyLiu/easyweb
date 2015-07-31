package org.easyweb;

import org.easyweb.util.DirectoryUtil;

import java.util.HashSet;
import java.util.Set;

public class Configuration {

    public static String DEPLOY_PATH = "easyweb.deploy";

    /**
     * 开发环境
     *
     * @return
     */
    public static boolean isDevMod() {
        return "dev".equals(System.getProperty("easyweb.env"));
    }

    public static boolean isMethodInterceptor() {
        return Boolean.getBoolean("easyweb.methodInterceptor");
    }

    private static Set<String> deployPaths;

    public static Set<String> getDeployPaths() {
        if (deployPaths == null) {
            String deployPath = System.getProperty(DEPLOY_PATH);
            if (deployPath == null) {
                throw new RuntimeException("System Property easyweb.deploy Required");
            }
            deployPaths = new HashSet<String>();
            for (String v : deployPath.split(",")) {
                deployPaths.add(DirectoryUtil.normalizePath(v.trim()));
            }
        }
        return deployPaths;
    }

    public static int getProfilerThreshold() {
        return Integer.getInteger("profiler.threshold", 2000);
    }

    public static String getRequestCharset() {
        return System.getProperty("easyweb.requestCharset", "UTF-8");
    }

    public static String getCodeCharset() {
        return System.getProperty("easyweb.codeCharset", "UTF-8");
    }


}
