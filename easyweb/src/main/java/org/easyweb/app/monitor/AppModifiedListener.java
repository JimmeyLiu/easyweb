package org.easyweb.app.monitor;

import org.easyweb.app.App;
import org.easyweb.app.AppFileHelper;
import org.easyweb.app.AppFileType;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.monitor.impl.FileAlterationListenerAdaptor;
import org.easyweb.app.monitor.impl.FileAlterationObserver;
import org.easyweb.util.EasywebLogger;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jimmey on 15-7-20.
 */
public class AppModifiedListener extends FileAlterationListenerAdaptor {
    AppDeployer appDeployer;
    App app;
    long lastModified;
    ScanResult scanResult;
    final AtomicBoolean deploying = new AtomicBoolean(false);

    public AppModifiedListener(App app, AppDeployer appDeployer) {
        this.app = app;
        this.appDeployer = appDeployer;
        this.lastModified = System.currentTimeMillis();
        this.scanResult = new ScanResult(app);
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        if (deploying.compareAndSet(false, true)) {
            final ScanResult result = scanResult.copyAndReset();
            if (!result.isModified()) {
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        appDeployer.deploy(app, result);
                    } catch (Exception e) {
                        EasywebLogger.error("Deploy " + app.getName() + " Error", e);
                    } finally {
                        deploying.set(false);
                    }
                }
            }).start();
        }
    }

    private void on(File file, boolean delete) {
        String filePath = file.getAbsolutePath();
        AppFileType type = AppFileHelper.getFileType(app, file);
        switch (type) {
            case BIZ_GROOVY:
                if (delete) {
                    scanResult.getBizGroovyFiles().remove(filePath);
                } else {
                    scanResult.addBizGroovyFile(filePath);
                }
                scanResult.setRestart(true);
                scanResult.setModified(true);
                break;
            case WEB_GROOVY:
                if (delete) {
                    scanResult.getWebGroovyFiles().remove(filePath);
                    scanResult.setRestart(true);
                } else {
                    scanResult.addModifiedWebGroovyFile(filePath);
                }
                scanResult.setModified(true);
                break;
            default:
                scanResult.addSuffixFile(file);
        }
    }

    @Override
    public void onFileCreate(File file) {
        on(file, false);
    }

    @Override
    public void onFileChange(File file) {
        on(file, false);
    }

    @Override
    public void onFileDelete(File file) {
        on(file, true);
    }

}
