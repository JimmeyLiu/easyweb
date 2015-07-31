package org.easyweb.app.monitor;

import org.easyweb.app.*;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.util.EasywebLogger;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jimmey on 15-7-31.
 */
public class AppAlterationListener {

    private App app;
    private AppDeployer deployer;
    private ScanResult scanResult;
    final AtomicBoolean deploying = new AtomicBoolean(false);

    public AppAlterationListener(App app, AppDeployer deployer) {
        this.app = app;
        this.deployer = deployer;
        this.scanResult = new ScanResult(app);

    }

    void onFinish() {
        if (deploying.compareAndSet(false, true)) {
            final ScanResult result = scanResult.copyAndReset();
            if (!result.isModified()) {
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        deployer.deploy(app, result);
                    } catch (Exception e) {
                        EasywebLogger.error("Deploy " + app.getName() + " Error", e);
                    } finally {
                        deploying.set(false);
                    }
                }
            }).start();
        }
    }

    void onCreate(AppFileType type, File file) {
        onChange(type, file);
    }

    void onChange(AppFileType type, File file) {
        switch (type) {
            case BIZ_GROOVY:
                scanResult.addBizGroovyFile(file.getAbsolutePath());
                scanResult.setRestart(true);
                scanResult.setModified(true);
                break;
            case WEB_GROOVY:
                scanResult.addModifiedWebGroovyFile(file.getAbsolutePath());
                scanResult.setModified(true);
                break;
            default:
                scanResult.addSuffixFile(file);
        }
    }

    void onDelete(AppFileType type, File file) {
        switch (type) {
            case BIZ_GROOVY:
                scanResult.getBizGroovyFiles().remove(file.getAbsolutePath());
                scanResult.setRestart(true);
                scanResult.setModified(true);
                break;
            case WEB_GROOVY:
                scanResult.getWebGroovyFiles().remove(file.getAbsolutePath());
                scanResult.setRestart(true);
                scanResult.setModified(true);
                break;
            default:
                scanResult.addSuffixFile(file);
        }
    }

}
