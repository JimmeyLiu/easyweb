package org.easyweb.app.deployfile;

import org.easyweb.Configuration;
import org.easyweb.Configuration;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午1:12
 */
public class DeployInfoFile {
    private static String infoFile = Configuration.getDeployPath() + "deploy_info";

    static {
        File file = new File(infoFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    public static void update(QueueItem item) {
        List<String> list = getFileContent();
        boolean contains = false;
        StringBuilder sb = new StringBuilder();
        for (String l : list) {
            if (item.containsAppKey(l)) {
                contains = true;
                sb.append(item.toString()).append("\n");
            } else {
                sb.append(l).append("\n");
            }
        }
        if (!contains) {
            sb.append(item.toString());
        }
        try {
            FileWriter writer = new FileWriter(infoFile);
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (Exception i) {
        }
    }

    public static List<QueueItem> getInfoList() {
        List<QueueItem> list = new ArrayList<QueueItem>();
        for (String line : getFileContent()) {
            QueueItem info = QueueItem.parse(line);
            if (info == null) {
                continue;
            }
            list.add(info);
        }
        return list;
    }

    private static List<String> getFileContent() {
        File file = new File(infoFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return IOUtils.readLines(inputStream);
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return Collections.emptyList();
    }


}
