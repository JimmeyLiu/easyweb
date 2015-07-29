package org.easyweb.app.deployfile;

import org.apache.commons.io.IOUtils;
import org.easyweb.Configuration;
import org.easyweb.app.DeployStatus;

import java.io.*;
import java.util.*;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午1:12
 */
@SuppressWarnings("all")
public class DeployQueueFile {

    private static String queueFile = Configuration.getDeployPath() + "deploy_queue";

    static {
        File file = new File(queueFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    public static void add(QueueItem queue) {
        try {
            FileWriter writer = new FileWriter(queueFile, true);
            writer.append(queue.toString()).append("\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
    }

    public static void update(QueueItem item) {
        item.setStatus(DeployStatus.DEPLOYING);
        add(item);
    }


    public static void remove(QueueItem item) {

        try {
            List<String> list = getFileContent();
            StringBuilder sb = new StringBuilder();
            for (String line : list) {
                if (item.containsAppKey(line) && line.contains(item.getPutQueueTime() + "")) {
                    continue;
                }
                sb.append(line).append("\n");
            }
            IOUtils.write(sb.toString(), new FileOutputStream(queueFile));
        } catch (Exception e) {

        }
    }

    public static List<QueueItem> getQueue() {
        List<String> lines = getFileContent();
        Map<String, List<QueueItem>> appMap = new HashMap<String, List<QueueItem>>();
        for (String line : lines) {
            QueueItem item = QueueItem.parse(line);
            if (item == null) {
                continue;
            }
            List<QueueItem> list = appMap.get(item.getAppName());
            if (list == null) {
                list = new ArrayList<QueueItem>();
                appMap.put(item.getAppName(), list);
            }
            list.add(item);
        }
        List<QueueItem> list = new ArrayList<QueueItem>();
        for (List<QueueItem> v : appMap.values()) {
            list.add(v.get(0));
        }

        return list;
    }

    private static List<String> getFileContent() {
        File file = new File(queueFile);
        try {
            return IOUtils.readLines(new FileInputStream(file));
        } catch (Exception e) {

        }
        return Collections.emptyList();
    }

}
