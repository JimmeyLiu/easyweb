package org.easyweb.app.modify;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.easyweb.app.App;
import org.easyweb.groovy.groovyobject.AppClassLoaderFactory;
import org.easyweb.request.assets.StaticCache;

import java.io.File;

/**
 * Created by jimmey on 15-7-20.
 */
public class AppModifiedListener extends FileAlterationListenerAdaptor {
    App app;
    long lastModified;

    public AppModifiedListener(App app) {
        this.app = app;
        this.lastModified = System.currentTimeMillis();
    }

    @Override
    public void onFileCreate(File file) {
        if (!file.getName().endsWith(".groovy")) {
            StaticCache.remote(file.getAbsolutePath());
            return;
        }

        boolean web = false;
        for (String p : app.getWebPaths()) {
            web = file.getParent().endsWith(p);
            if (web) break;
        }
        if (web) {

        }
    }

    @Override
    public void onFileChange(File file) {
        System.out.println(file.getName());
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println(file.getName());
    }

}
