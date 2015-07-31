package org.easyweb.orm.nutz;

import org.easyweb.util.EasywebLogger;
import org.nutz.log.Log;
import org.nutz.log.LogAdapter;
import org.nutz.log.impl.AbstractLog;
import org.nutz.plugin.Plugin;

/**
 * Created by jimmey on 15-7-31.
 */
public class Slf4jAdapter implements LogAdapter, Plugin {
    @Override
    public boolean canWork() {
        return true;
    }

    static Slf4jLog log = new Slf4jLog();

    @Override
    public Log getLogger(String className) {
        return log;
    }

    static class Slf4jLog extends AbstractLog {

        protected void log(int level, Object message, Throwable tx) {
            String msg = String.valueOf(message);
            switch (level) {
                case LEVEL_TRACE:
                case LEVEL_DEBUG:
                    EasywebLogger.debug(msg, tx);
                    break;
                case LEVEL_INFO:
                    EasywebLogger.info(msg, tx);
                    break;
                case LEVEL_WARN:
                    EasywebLogger.warn(msg, tx);
                    break;
                case LEVEL_ERROR:
                case LEVEL_FATAL:
                    EasywebLogger.error(msg, tx);
                    break;
            }
        }

        @Override
        public void fatal(Object message, Throwable t) {
            log(LEVEL_FATAL, message, t);
        }

        @Override
        public void error(Object message, Throwable t) {
            log(LEVEL_ERROR, message, t);
        }

        @Override
        public void warn(Object message, Throwable t) {
            log(LEVEL_WARN, message, t);
        }

        @Override
        public void info(Object message, Throwable t) {
            log(LEVEL_INFO, message, t);
        }

        @Override
        public void debug(Object message, Throwable t) {
            log(LEVEL_DEBUG, message, t);
        }

        @Override
        public void trace(Object message, Throwable t) {
            log(LEVEL_TRACE, message, t);
        }
    }

}
