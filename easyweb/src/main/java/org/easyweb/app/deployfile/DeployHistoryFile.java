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
 * Time: 下午1:13
 */
public class DeployHistoryFile {

    private static String historyFile = Configuration.getDeployPath() + "deploy_history";
    static {
        File file = new File(historyFile);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
    }
    public static void add(History history) {
        File file = new File(historyFile);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file,true);
            writer.append(history.toString()).append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }

    public static List<History> getHistory(String appName, String appVersion) {
        List<String> lines = getFileContent();
        List<History> histories = new ArrayList<History>();
        String key = appName + Base.SPLIT + appVersion;
        for (String line : lines) {
            if (!line.startsWith(key)) {
                continue;
            }
            History history = History.parse(line);
            if (history != null) {
                histories.add(history);
            }
        }
        return histories;
    }

    private static List<String> getFileContent() {
        File file = new File(historyFile);
        try {
            return IOUtils.readLines(new FileInputStream(file));
        } catch (Exception e) {

        }
        return Collections.emptyList();
    }

}
