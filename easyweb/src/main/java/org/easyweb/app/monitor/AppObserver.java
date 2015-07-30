package org.easyweb.app.monitor;

import org.easyweb.app.App;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.monitor.impl.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by jimmey on 15-7-20.
 */
public class AppObserver extends FileAlterationObserver {

    public AppObserver(final App app, AppDeployer appDeployer) {
        super(app.getRootPath(), new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.getAbsolutePath().startsWith(app.getClasspath()) && !pathname.getName().startsWith(".");
            }
        });
        addListener(new AppModifiedListener(app, appDeployer));
    }

}
