package org.easyweb.request.render.param;

import groovyjarjarasm.asm.*;
import org.apache.commons.lang.StringUtils;
import org.easyweb.annocation.Param;
import org.easyweb.annocation.PathVariable;
import org.easyweb.annocation.RequestBean;
import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.profiler.Profiler;
import org.easyweb.request.uri.UriTemplate;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午3:13
 */
public class ParamBuilder {


    public static Object[] build(UriTemplate uriTemplate) {
        Method method = uriTemplate.getPageMethod().getMethod();
        Map<String, String> pathParams = uriTemplate.match(ThreadContext.getContext().getRequest().getRequestURI());
        return build(method, pathParams);
    }

    /**
     * 根据request构建groovy执行的参数
     *
     * @param method
     * @param pathParams restful 参数
     * @return
     */
    private static Object[] build(Method method, Map<String, String> pathParams) {
        try {
            Profiler.enter("start build params");
            Class<?>[] types = method.getParameterTypes();
            if (types.length == 0) {
                return new Object[0];
            }


            Object[] params = new Object[types.length];
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < types.length; i++) {
                Object v = null;
                Class<?> type = types[i];
                Annotation[] typeAnnotations = annotations[i];
                if (type.equals(Context.class)) {
                    v = ThreadContext.getContext();
                } else if (typeAnnotations == null || typeAnnotations.length == 0) {
                    //没有注解，则直接注入null
                } else {
                    for (Annotation annotation : typeAnnotations) {
                        if (annotation instanceof Param) {
                            Param param = (Param) annotation;
                            String name = param.name();
                            if (StringUtils.isBlank(name)) {
                                name = param.value();
                            }
                            v = TypeCovert.convert(type, getParameterValues(name), param.defaultValue());
                        } else if (annotation instanceof RequestBean) {
                            v = TypeCovert.beanConvert(type);
                        } else if (annotation instanceof PathVariable) {//restful参数
                            if (pathParams != null) {
                                PathVariable pathVariable = (PathVariable) annotation;
                                String name = pathVariable.value();
                                String temp = pathParams.get(name);
                                v = TypeCovert.convert(type, new String[]{temp}, null);
                            }
                        }
                    }
                }
                params[i] = v;
            }


            return params;
        } finally {
            Profiler.release();
        }
    }

    private static String[] getParameterValues(String name) {
        String[] values = ThreadContext.getContext().getRequest().getParameterValues(name);
        return values;
    }

    public static String[] getMethodParamNames(final Method m) {
        final String[] paramNames = new String[m.getParameterTypes().length];
        final String n = m.getDeclaringClass().getName();
        ClassReader cr = null;
        try {
            cr = new ClassReader(n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cr.accept(new ClassVisitor(Opcodes.ASM4) {
            @Override
            public MethodVisitor visitMethod(final int access,
                                             final String name, final String desc,
                                             final String signature, final String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(desc);
                // 方法名相同并且参数个数相同
                if (!name.equals(m.getName())
                        || !sameType(args, m.getParameterTypes())) {
                    return super.visitMethod(access, name, desc, signature,
                            exceptions);
                }
                MethodVisitor v = super.visitMethod(access, name, desc,
                        signature, exceptions);
                return new MethodVisitor(Opcodes.ASM4, v) {
                    @Override
                    public void visitLocalVariable(String name, String desc,
                                                   String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(m.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < paramNames.length) {
                            paramNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start,
                                end, index);
                    }

                };
            }
        }, 0);
        return paramNames;
    }

    /**
     * <p>
     * 比较参数类型是否一致
     * </p>
     *
     * @param types   asm的类型({@link Type})
     * @param clazzes java 类型({@link Class})
     * @return
     */
    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
        // 个数不同
        if (types.length != clazzes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(clazzes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

}
