package org.easyweb.groovy.groovyobject;

import org.easyweb.groovy.metamethod.MetaMethodFactory;
import org.easyweb.groovy.metamethod.MetaMethodFactory;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.ProxyMetaClass;

import java.beans.IntrospectionException;

/**
 * Created with IntelliJ IDEA. User: jimmey Date: 12-11-25 Time: 下午6:34
 * <p/>
 * MetaClass代理类，继承自
 *
 * @see ProxyMetaClass <p/>
 *      增加内部提供的方法
 */
public class MetaClassProxy extends ProxyMetaClass {

    /**
     * convenience factory method for the most usual case.
     */

    public static MetaClassProxy getMyInstance(Class<?> theClass) throws IntrospectionException {
        MetaClassRegistry metaRegistry = GroovySystem.getMetaClassRegistry();
        MetaClass meta = metaRegistry.getMetaClass(theClass);
        return new MetaClassProxy(metaRegistry, theClass, meta);
    }

    /**
     * @param adaptee the MetaClass to decorate with interceptability
     */
    public MetaClassProxy(MetaClassRegistry registry, Class<?> theClass, MetaClass adaptee) throws IntrospectionException {
        super(registry, theClass, adaptee);
    }

    /**
     * 对调用的方法进行拦截
     *
     * @param object
     * @param methodName
     * @param arguments
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object invokeMethod(final Object object, final String methodName, final Object[] arguments) {
        Object obj = MetaMethodFactory.invoke(methodName, arguments);
        if (obj != null) {
            return obj;
        }
        return super.invokeMethod(object, methodName, arguments);
    }
//
//    public void putContext(String key, Object value) {
//        ThreadContext.getContext().putContext(key, value);
//    }
//
//    public void setLayout(String layout) {
//        if (layout.endsWith(".vm")) {
//            ThreadContext.getContext().setLayout(layout);
//        }
//    }
//
//    public void putAll(Map<String, Object> map) {
//        ThreadContext.getContext().putAll(map);
//    }
//
//    public HttpServletRequest request() {
//        return ThreadContext.getContext().getRequest();
//    }
//
//    public String getString(String key) {
//        return request().getParameter(key);
//    }
//
//    public String getString(String key, String defVal) {
//        String v = getString(key);
//        return v == null ? defVal : v;
//    }
//
//    public int getInt(String key) {
//        String v = request().getParameter(key);
//        return v != null ? toInt(v) : 0;
//    }
//
//    public int getInt(String key, int defVal) {
//        int v = getInt(key);
//        return v > 0 ? v : defVal;
//    }
//
//    public long getLong(String key) {
//        String v = request().getParameter(key);
//        return v != null ? toLong(v) : 0;
//    }
//
//    public long getLong(String key, long defVal) {
//        long v = getLong(key);
//        return v > 0 ? v : defVal;
//    }
//
//    /**
//     * 获取数组参数
//     *
//     * @param key
//     * @return
//     */
//    public String[] getArray(String key) {
//        return request().getParameterValues(key);
//    }
//
//	/* String向其他类型转化 */
//
//    public int toInt(String string) {
//        try {
//            return Integer.valueOf(string);
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    public long toLong(String string) {
//        try {
//            return Long.valueOf(string);
//        } catch (Exception e) {
//            return 0;
//        }
//    }

}
