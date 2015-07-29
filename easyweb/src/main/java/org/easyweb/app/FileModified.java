package org.easyweb.app;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午1:25
 */
public class FileModified {
    static Map<String, Long> modified = new ConcurrentHashMap<String, Long>();

    public static boolean isModified(File file) {
        String name = file.getAbsolutePath();
        long last = file.lastModified();
        Long l = modified.get(name);
        if (l == null || last != l.longValue()) {
            modified.put(name, last);
            return true;
        }
        return false;
    }
}
