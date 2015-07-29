package org.easyweb.app.modify;

import org.apache.commons.io.monitor.FileAlterationObserver;
import org.easyweb.app.App;

/**
 * Created by jimmey on 15-7-20.
 */
public class AppObserver extends FileAlterationObserver {

    public AppObserver(App app) {
        super(app.getRootPath());
        addListener(new AppModifiedListener(app));
    }

}
