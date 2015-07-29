package org.easyweb.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by jimmey on 15-7-20.
 */
public class AppLookupFactory {

    private static List<AppLookup> lookups = new ArrayList<AppLookup>();
    static AppLookup lookup = new DefaultLookup();

    static {
        Iterator<AppLookup> it = ServiceLoader.load(AppLookup.class).iterator();
        while (it.hasNext()) {
            lookups.add(it.next());
        }
    }

    public static App lookup(String serverName, String uri) {
        if (!lookups.isEmpty()) {
            for (AppLookup appLookup : lookups) {
                App app = appLookup.lookup(serverName, uri);
                if (app != null) {
                    return app;
                }
            }
        }
        return lookup.lookup(serverName, uri);
    }

    static class DefaultLookup implements AppLookup {
        @Override
        public App lookup(String serverName, String uri) {
            String[] v = uri.split("/");
            String app = v[1];
            return AppContainer.getApp(app);
        }
    }

}
