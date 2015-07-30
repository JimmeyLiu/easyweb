package org.easyweb.request.assets;

import org.easyweb.util.EasywebLogger;
import org.easyweb.util.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticCache {

    private static Map<String, AssetsData> map = new ConcurrentHashMap<String, AssetsData>();

    public static AssetsData get(String key) {
        AssetsData data = map.get(key);
        try {
            if (data != null) {
                return data;
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (!key.startsWith("file:")) {
                data = new AssetsData(0, new byte[0]);
            }
            File file = new File(key.replace("file:", ""));
            if (!file.exists()) {
                data = new AssetsData(0, new byte[0]);
            }
            InputStream input = new FileInputStream(file);
            IOUtils.copy(input, stream);
            data = new AssetsData(file.lastModified(), stream.toByteArray());
            map.put(key, data);
        } catch (Exception e) {
            EasywebLogger.error("StaticCache error", e);
        }
        return data;
    }

    public static void remove(String key) {
        map.remove(key);
    }
}
