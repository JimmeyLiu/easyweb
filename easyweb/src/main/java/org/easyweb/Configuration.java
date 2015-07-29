package org.easyweb;

import org.easyweb.app.App;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.net.InetAddress;
import java.util.Properties;

public class Configuration {

    private static Logger logger = LoggerFactory.getLogger(Configuration.class);

    public static String PATH = "file.resource.loader.path";
    public static String CACHE = "file.resource.loader.cache";
    public static String CHECK = "file.resource.loader.modificationCheckInterval";
    public static String APP_DEPLOY_PATH = "app.deploy.path";
    public static String METHOD_INTERCEPTOR = "method.interceptor";
    /**
     * 环境模式，值有
     * 1.dev
     * 2.daily
     * 3.online
     */
    public static String envMode = "envMode";
    public static String httpCharset = "httpCharset";
    public static String velocityCharset = "velocityCharset";
    public static String httpSuffix = "httpSuffix";
    public static String deployPath;

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("easyweb_default.properties"));
            // 外部配置的
            Properties config = new Properties();
            config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("easyweb.properties"));

            logger.error("Load Classpath Config: " + config.toString());

            set(config, PATH);
            set(config, CACHE);
            set(config, CHECK);
            set(config, APP_DEPLOY_PATH);
            set(config, envMode);
            set(config, httpCharset);
            set(config, "profiler.threshold");
            set(config, METHOD_INTERCEPTOR);
            set(config, velocityCharset);
            String mode = System.getProperty("easyweb.env.mode");
            if (StringUtils.isNotBlank(mode)) {
                properties.setProperty(mode, mode);
            }

            String ewRoot = System.getProperty("easyweb.root.path");
            if (StringUtils.isNotBlank(ewRoot)) {
                properties.setProperty(APP_DEPLOY_PATH, ewRoot + "/apps");
            }
        } catch (Exception e) {
            logger.error("Load Config Error", e);
        }
    }

    private static void set(Properties config, String name) {
        if (config.getProperty(name) != null) {
            properties.setProperty(name, config.getProperty(name));
        }
    }

    public static String getEnvMode() {
        String mode = properties.getProperty(envMode, "online");
        if (!mode.equals("dev") && !mode.equals("daily") && !mode.equals("online")) {
            mode = "online";
        }
        return mode;
    }

    /**
     * 开发环境
     *
     * @return
     */
    public static boolean isDev() {
        return "dev".equals(getEnvMode());
    }

    /**
     * 测试环境
     *
     * @return
     */
    public static boolean isDaily() {
        return "daily".equals(getEnvMode());
    }

    /**
     * 生成环境，线上、预发
     *
     * @return
     */
    public static boolean isOnline() {
        return "online".equals(getEnvMode());
    }

    public static boolean isMethodInterceptor() {
        return "true".equalsIgnoreCase(getValue(METHOD_INTERCEPTOR, "false"));
    }

    public static String getDeployPath() {
        if (deployPath == null) {
            deployPath = getDeployPathWithoutSlash() + "/";
        }
        return deployPath;
    }

    public static String getClasspath(App app) {
        return getClasspathRoot() + app.getAppKey();
    }

    private static String getClasspathRoot() {
        return getDeployPathWithoutSlash() + "_classes/";
    }

    public static String getDeployPathWithoutSlash() {
        String v = getValue(APP_DEPLOY_PATH, System.getProperty("user.home") + "/easyweb/apps");
        if (v.endsWith("/")) {
            v = v.substring(0, v.length() - 1);
        }
        return v;
    }

    public static String getDeployTempPath() {
        return getDeployPathWithoutSlash() + "_temp/";
    }

    public static String getDeployBakPath() {
        return getDeployPathWithoutSlash() + "_bak/";
    }

    public static int getProfilerThreshold() {
        return getInt("profiler.threshold", 2000);
    }

    private static String getValue(String key) {
        return properties.getProperty(key);
    }

    private static String getValue(String key, String defVal) {
        return properties.getProperty(key, defVal);
    }

    private static int getInt(String key) {
        String v = getValue(key);
        try {
            return Integer.valueOf(v);
        } catch (Exception e) {
            return 0;
        }
    }

    private static int getInt(String key, int defVal) {
        int v = getInt(key);
        return v > 0 ? v : defVal;
    }

    public static String getHttpCharset() {
        return getValue(httpCharset, "GBK");
    }

    public static String getHttpSuffix() {
        return getValue(httpSuffix, "html");
    }

    public static String getVelocityCharset() {
        return getValue(velocityCharset, "GBK");
    }


}
