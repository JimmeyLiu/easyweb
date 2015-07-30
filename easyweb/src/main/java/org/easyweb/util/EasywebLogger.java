package org.easyweb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Created with IntelliJ IDEA. User: jimmey Date: 12-11-25 Time: 下午4:30 To
 * change this template use File | Settings | File Templates.
 */
public class EasywebLogger {

    private static Logger logger = LoggerFactory.getLogger("easyweb");

    public static void initMDC(String key, String value) {
        MDC.put(key, value);
    }

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void error(String fmt, Object... args) {
        error(String.format(fmt, args));
    }

    public static void error(String msg, Throwable e) {
        logger.error(msg, e);
    }

    public static void error(Throwable e) {
        error(e.getMessage(), e);
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void info(String fmt, Object... args) {
        info(String.format(fmt, args));
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void warn(String fmt, Object... args) {
        warn(String.format(fmt, args));
    }

    public static void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    public static void debug(String fmt, Object... args) {
        debug(String.format(fmt, args));
    }


}
