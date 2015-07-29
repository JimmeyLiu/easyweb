package org.easyweb.app.deploy;

import org.easyweb.Configuration;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.app.change.AppChangeAdapter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午10:07
 */
public class ZipBackup extends AppChangeAdapter {

    private static String BACKUP_PATH = Configuration.getDeployBakPath();

    @Override
    public void init() {
        File back = new File(BACKUP_PATH);
        if (!back.exists()) {
            back.mkdirs();
        }
    }

    @Override
    public void success(App app) {
        File file = new File(app.getRootPath());
        if (!file.canRead()) {
            return;
        }

        String zipFile = file.getName() + ".zip";
        File backZip = new File(BACKUP_PATH + zipFile);
        if (backZip.canRead()) {
            return;
        }

        File src = new File(Configuration.getDeployTempPath() + zipFile);
        if (!src.canRead()) {
            return;
        }

        try {
            IOUtils.copy(new FileInputStream(src), new FileOutputStream(backZip));
        } catch (Exception e) {
        }
    }
}
