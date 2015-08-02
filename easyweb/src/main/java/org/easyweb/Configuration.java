package org.easyweb;

import org.easyweb.util.DirectoryUtil;

import java.util.HashSet;
import java.util.Set;

public class Configuration {

    public static String DEPLOY_PATH = "easyweb.deploy";
    public static String ENV_MODE = "easyweb.env";
    public static String DEPLOY_TOKEN = "easyweb.token";

    /**
     * 开发环境
     *
     * @return
     */
    public static boolean isDevMod() {
        return "dev".equals(System.getProperty(ENV_MODE));
    }

    private static Set<String> deployPaths;

    private static String appsRoot;

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

    public static String getAppsRoot() {
        return appsRoot;
    }

    public static void setAppsRoot(String appsRoot) {
        Configuration.appsRoot = appsRoot;
    }

    public static String getRequestCharset() {
        return System.getProperty("easyweb.requestCharset", "UTF-8");
    }

    public static String getCodeCharset() {
        return System.getProperty("easyweb.codeCharset", "UTF-8");
    }

    public static String getDeployToken() {
        return System.getProperty(DEPLOY_TOKEN, "ew");
    }


}
