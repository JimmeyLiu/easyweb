package org.easyweb.groovy.metamethod;

import com.alibaba.fastjson.JSON;
import org.easyweb.context.ThreadContext;
import org.easyweb.context.ThreadContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jimmey
 * Date: 13-3-26
 * Time: 下午11:40
 * To change this template use File | Settings | File Templates.
 */
public class MetaMethodImpl implements IMetaMethod {
    @Override
    public Object invoke(String methodName, Object[] arguments) {
        if ("putContext".equals(methodName)) {
            putContext((String) arguments[0], arguments[1]);
            return Void.TYPE;
        }
        if ("toJson".equals(methodName)) {
            ThreadContext.getContext().getResponse().setContentType("application/json");
            return JSON.toJSONString(arguments[0]);
        }
        if ("putAll".equals(methodName)) {
            putAll((Map<String, Object>) arguments[0]);
            return Void.TYPE;
        }
        if ("request".equals(methodName)) {
            return request();
        }

        if ("getString".equals(methodName)) {
            if (arguments.length == 1) {
                return getString((String) arguments[0]);
            } else if (arguments.length == 2) {
                return getString((String) arguments[0], (String) arguments[1]);
            }
        }

        if ("getInt".equals(methodName)) {
            if (arguments.length == 1) {
                return getInt((String) arguments[0]);
            } else if (arguments.length == 2) {
                return getInt((String) arguments[0], (Integer) arguments[1]);
            }
        }

        if ("getLong".equals(methodName)) {
            if (arguments.length == 1) {
                return getLong((String) arguments[0]);
            } else if (arguments.length == 2) {
                return getLong((String) arguments[0], (Long) arguments[1]);
            }
        }

        if ("getArray".equals(methodName)) {
            return getArray((String) arguments[0]);
        }

        if ("toInt".equals(methodName)) {
            return toInt((String) arguments[0]);
        }

        if ("toLong".equals(methodName)) {
            return toLong((String) arguments[0]);
        }

        if ("setLayout".equals(methodName)) {
            setLayout((String) arguments[0]);
            return Void.TYPE;
        }

        if ("redirect".equals(methodName)) {
            ThreadContext.getContext().setRedirectTo((String) arguments[0]);
            return Void.TYPE;
        }

        if("setDownload".equals(methodName)){
            ThreadContext.getContext().setDownload(true);
            return Void.TYPE;
        }
        return null;
    }


    public void putContext(String key, Object value) {
        ThreadContext.getContext().putContext(key, value);
    }

    public void setLayout(String layout) {
        if (layout.endsWith(".vm")) {
            ThreadContext.getContext().setLayout(layout);
        }
    }

    public void putAll(Map<String, Object> map) {
        ThreadContext.getContext().putAll(map);
    }

    public HttpServletRequest request() {
        return ThreadContext.getContext().getRequest();
    }

    public String getString(String key) {
        return request().getParameter(key);
    }

    public String getString(String key, String defVal) {
        String v = getString(key);
        return v == null ? defVal : v;
    }

    public int getInt(String key) {
        String v = request().getParameter(key);
        return v != null ? toInt(v) : 0;
    }

    public int getInt(String key, int defVal) {
        int v = getInt(key);
        return v > 0 ? v : defVal;
    }

    public long getLong(String key) {
        String v = request().getParameter(key);
        return v != null ? toLong(v) : 0;
    }

    public long getLong(String key, long defVal) {
        long v = getLong(key);
        return v > 0 ? v : defVal;
    }

    /**
     * 获取数组参数
     *
     * @param key
     * @return
     */
    public String[] getArray(String key) {
        return request().getParameterValues(key);
    }

	/* String向其他类型转化 */

    public int toInt(String string) {
        try {
            return Integer.valueOf(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public long toLong(String string) {
        try {
            return Long.valueOf(string);
        } catch (Exception e) {
            return 0;
        }
    }
}
