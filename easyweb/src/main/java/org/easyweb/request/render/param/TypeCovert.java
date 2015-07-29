package org.easyweb.request.render.param;

import org.easyweb.context.ThreadContext;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午4:15
 */
public class TypeCovert {


//    public static Object typeCovert(Param param, Type type) {
//        String name = param.name();
//        if (StringUtils.isBlank(name)) {
//            name = param.value();
//        }
//        if (type instanceof Class<?>) {
//            Class<?> clazz = (Class<?>) type;
//            if (clazz.isArray()) {
//                Class<?> componentClazz = clazz.getComponentType();
//                String[] values = ThreadContext.getContext().getRequest().getParameterValues(name);
//                if (values != null) {
//                    Object array = Array.newInstance(componentClazz, values.length);
//                    for (int i = 0; i < values.length; i++) {
//                        Array.set(array, i, convert(param, values[i], componentClazz));
//                    }
//                    return array;
//                }
//                return null;
//            } else {
//                String value = ThreadContext.getContext().getRequest().getParameter(name);
//                return convert(param, value, clazz);
//            }
//        }
//        return null;
//    }

    public static Object beanConvert(Type beanType) {
        if (beanType instanceof Class<?>) {
            Class<?> clazz = (Class<?>) beanType;
            try {
                Object bean = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field it : fields) {
                    it.setAccessible(true);
                    String name = it.getName();
                    Class<?> type = it.getType();
                    String[] requestValue = getParameterValues(name);
                    if (requestValue != null) {
                        Object propertyValue = convert(type, requestValue, null);
                        if (propertyValue != null) {
                            it.set(bean, propertyValue);
                        }
                    }
                }
                return bean;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    private static String[] getParameterValues(String name) {
        return ThreadContext.getContext().getRequest().getParameterValues(name);
    }

    public static Object convert(Class<?> clazz, String[] values, String defaultValue) {
        if (clazz.isArray()) {
            Class<?> componentClazz = clazz.getComponentType();
            if (values != null) {
                Object array = Array.newInstance(componentClazz, values.length);
                for (int i = 0; i < values.length; i++) {
                    Array.set(array, i, inconvert(componentClazz, values[i], defaultValue));
                }
                return array;
            }
            return null;
        }
        String value = values == null || values.length == 0 ? null : values[0];
        return inconvert(clazz, value, defaultValue);
    }

    private static Object inconvert(Class<?> clazz, String value, String defaultValue) {
        if (clazz.isPrimitive()) {
            clazz = PRIMITIVES.get(clazz.getName());
        }

        if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
            try {
                return Integer.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(defaultValue)) {
                    return Integer.valueOf(defaultValue);
                }
            }
        } else if (clazz.equals(Long.class) || clazz.equals(Long.TYPE)) {
            try {
                return Long.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(defaultValue)) {
                    return Long.valueOf(defaultValue);
                }
            }
        } else if (clazz.equals(Double.class) || clazz.equals(Double.TYPE)) {
            try {
                return Double.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(defaultValue)) {
                    return Double.valueOf(defaultValue);
                }
            }
        } else if (clazz.equals(Float.class) || clazz.equals(Float.TYPE)) {
            try {
                return Float.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(defaultValue)) {
                    return Float.valueOf(defaultValue);
                }
            }
        } else if (clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE)) {
            try {
                return Boolean.valueOf(value);
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(defaultValue)) {
                    return Boolean.valueOf(defaultValue);
                }
            }
        } else {
            if (value != null) {
                return value;
            } else if (StringUtils.isNotBlank(defaultValue)) {
                return defaultValue;
            }
        }
        return null;
    }

    private static final Map<String, Class<?>> PRIMITIVES = new HashMap<String, Class<?>>();

    static {
        PRIMITIVES.put(boolean.class.getName(), Boolean.class);
        PRIMITIVES.put(long.class.getName(), Long.class);
        PRIMITIVES.put(int.class.getName(), Integer.class);
        PRIMITIVES.put(double.class.getName(), Double.class);
        PRIMITIVES.put(float.class.getName(), Float.class);
        PRIMITIVES.put(byte.class.getName(), Byte.class);
        PRIMITIVES.put(char.class.getName(), Character.class);
    }
}
