package org.easyweb.groovy.groovyobject;

import groovy.lang.*;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.bean.BeanFactory;
import org.easyweb.groovy.MethodInterceptor;
import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.groovy.annotation.AnnotationParserFactory;
import org.easyweb.util.EasywebLogger;
import org.easyweb.velocity.GroovyVelocityEngine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 上午12:07
 * <p/>
 * groovy对象加载类，和具体的classloader关联，并且classloader会和文件的类型对应
 */
@Component
public class GroovyObjectLoader {

    @Resource
    private MethodInterceptor methodInterceptor;
    @Resource(name = "ewBinding")
    private BeanBinding binding;
    @Resource(name = "ewGroovyVelocityEngine")
    private GroovyVelocityEngine groovyVelocityEngine;

    /**
     * 实例化app中biz下的所有对象
     *
     * @param webClass 是否是web groovy，会根据这个参数获取web classloader还是biz classloader
     * @param app
     * @param file
     */
    public void instanceObject(boolean webClass, App app, File file) {
        GroovyClassLoader classLoader = getByType(webClass, app);
        CacheDO cacheDO = Cacher.get(classLoader, file);
        String path = file.getAbsolutePath();
        if (cacheDO == null) {
            try {
                EasywebLogger.warn("load file " + path);
                Class<?> clazz = null;
                if (webClass) {//对应两种不同的load方式
                    try {
                        GroovyCodeSource codeSource = new GroovyCodeSource(file, Configuration.getCodeCharset());
                        clazz = AppClassLoaderFactory.getAppWebClassLoader(app).parseClass(codeSource, false);
                    } catch (Exception e) {
                        EasywebLogger.error("init error:" + path, e);
                    }
                } else {
                    clazz = classLoader.loadClass(FileMainClass.get(file.getAbsolutePath()));
                }

                GroovyObject obj = (GroovyObject) clazz.newInstance();
                parseAnnotation(AnnotationParser.ParsePhase.Init, app, file, obj);
                cacheDO = new CacheDO();
                cacheDO.setLastModified(file.lastModified());
                cacheDO.setObj(obj);
                cacheDO.setAutowired(false);
                Cacher.put(classLoader, file, cacheDO);
            } catch (ClassNotFoundException e) {
                EasywebLogger.error("init error " + path, e);
            } catch (InstantiationException e) {
                EasywebLogger.error("init error" + path, e);
            } catch (IllegalAccessException e) {
                EasywebLogger.error("init error" + path, e);
            }
        }
    }

    private void autowired(boolean webClass, App app, File file, CacheDO cacheDO) {
        if (cacheDO == null) {
            GroovyClassLoader classLoader = getByType(webClass, app);
            cacheDO = Cacher.get(classLoader, file);
            if (cacheDO == null) {
                return;
            }
        }
        if (cacheDO.isAutowired()) {
            return;
        }
        GroovyObject object = cacheDO.getObj();
        binding.setProperty("vm", groovyVelocityEngine);
        if (object instanceof Script) {
            Script script = (Script) object;
            script.setBinding(binding);
        } else {
            InvokerHelper.setProperties(object, binding.getVariables());
            InvokerHelper.setProperties(object, BeanFactory.getBeans(app));
        }
        parseAnnotation(AnnotationParser.ParsePhase.Ioc, app, file, object);
        MetaClassProxy proxy;
        try {
            proxy = MetaClassProxy.getMyInstance(object.getClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        proxy.setInterceptor(methodInterceptor);
        object.setMetaClass(proxy);
        cacheDO.setAutowired(true);

        try {
            //设置属性后调用init方法
            object.invokeMethod("init", null);
        } catch (Exception e) {
        }

    }

    public void autowiredObject(boolean webClass, App app, File file) {
        autowired(webClass, app, file, null);
    }

    public GroovyObject getObject(boolean webClass, App app, File file) {
        GroovyClassLoader classLoader = getByType(webClass, app);
        CacheDO cacheDO = Cacher.get(classLoader, file);
        if (cacheDO == null) {//如果没有，先执行一下
            instanceObject(webClass, app, file);
            autowired(webClass, app, file, null);
        } else if (!cacheDO.isAutowired()) {
            autowired(webClass, app, file, cacheDO);
        } else {
            return cacheDO.getObj();
        }
        cacheDO = Cacher.get(classLoader, file);
        return cacheDO.getObj();
    }

    private GroovyClassLoader getByType(boolean webClass, App app) {
        if (webClass) {
            return AppClassLoaderFactory.getAppWebClassLoader(app);
        } else {
            return AppClassLoaderFactory.getAppBizClassLoader(app);
        }
    }

    private void parseAnnotation(AnnotationParser.ParsePhase parsePhase, App app, File file, GroovyObject object) {
        if (object instanceof Script) {
            Script script = (Script) object;
            List<MetaMethod> metaMethods = script.getMetaClass().getMethods();
            for (MetaMethod method : metaMethods) {
                if (method instanceof CachedMethod) {
                    Method javaMethod = ((CachedMethod) method).getCachedMethod();
                    Annotation[] annotations = javaMethod.getAnnotations();
                    for (Annotation annotation : annotations) {
                        AnnotationParserFactory.parse(parsePhase, app, annotation, file, javaMethod, object);
                    }
                }
            }
        } else {
            Annotation[] annotations = object.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                AnnotationParserFactory.parse(parsePhase, app, annotation, file, object.getClass(), object);
            }
            Method[] methods = object.getClass().getDeclaredMethods();
            for (Method method : methods) {
                Annotation[] ans = method.getAnnotations();
                for (Annotation annotation : ans) {
                    AnnotationParserFactory.parse(parsePhase, app, annotation, file, method, object);
                }
            }
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                Annotation[] ans = field.getAnnotations();
                for (Annotation annotation : ans) {
                    AnnotationParserFactory.parse(parsePhase, app, annotation, file, field, object);
                }
            }
        }
    }

}
