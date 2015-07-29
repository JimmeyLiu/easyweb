package org.easyweb.groovy.groovyobject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 上午10:23
 * <p/>
 * 在groovy文件编译的时候记录文件在classloader中的名称
 */
public class FileMainClass {
    private static Map<String, String> fileMainClass = new ConcurrentHashMap<String, String>();

    public static void set(String file, String mainClass) {
        fileMainClass.put(file, mainClass);
    }

    public static String get(String file) {
        return fileMainClass.get(file);
    }

}
