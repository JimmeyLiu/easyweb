package org.easyweb;

public class Configuration {

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

    private static String deployPath;

    public static String getDeployPath() {
        if (deployPath == null) {
            deployPath = System.getProperty("easyweb.deployPath", System.getProperty("user.home") + "/easyweb/apps");
            if (!deployPath.endsWith("/")) {
                deployPath += "/";
            }
        }
        return deployPath;
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
