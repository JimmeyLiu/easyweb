package org.easyweb.groovy.metamethod;

import org.easyweb.context.ThreadContext;
import org.easyweb.context.ThreadContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jimmey
 * Date: 13-3-26
 * Time: 下午11:30
 * To change this template use File | Settings | File Templates.
 */
public class MetaMethodFactory {

    private static Map<String, IMetaMethod> appMetaMethods = new ConcurrentHashMap<String, IMetaMethod>();
    private static IMetaMethod defaultImpl;

    static {
        defaultImpl = new MetaMethodImpl();
    }

    public static void register(String appName, IMetaMethod iMetaMethod) {
        appMetaMethods.put(appName, iMetaMethod);
    }

    public static Object invoke(final String methodName, final Object[] arguments) {
        IMetaMethod metaMethod = appMetaMethods.get(ThreadContext.getContext().getAppName());
        Object obj = null;
        if (metaMethod != null) {
            obj = metaMethod.invoke(methodName, arguments);
        }
        if (obj == null) {
            obj = defaultImpl.invoke(methodName, arguments);
        }
        return obj;
    }

}
