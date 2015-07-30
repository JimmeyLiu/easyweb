package org.easyweb.request.render;

import org.easyweb.app.App;
import org.easyweb.app.AppContainer;
import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.groovy.GroovyEngine;
import org.easyweb.profiler.Profiler;
import org.easyweb.request.PageMethod;
import org.easyweb.request.render.param.ParamBuilder;
import org.easyweb.request.uri.UriTemplate;
import org.easyweb.util.DirectoryUtil;
import org.easyweb.velocity.AppVmFile;
import org.easyweb.velocity.VelocityEngine;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * groovy代码执行入口
 *
 * @author jimmey
 */
public class CodeRender {

    private GroovyEngine groovyEngine;
    private VelocityEngine velocityEngine;

    private CodeRender() {
        this.groovyEngine = new GroovyEngine();
        this.velocityEngine = new VelocityEngine();
    }

    private static CodeRender instance;

    public static CodeRender getInstance() {
        if (instance == null) {
            instance = new CodeRender();
        }
        return instance;
    }

    public String renderPage(UriTemplate uriTemplate, Map<String, Object> inpurtParams) throws Exception {
        StringWriter writer = new StringWriter();
        PageMethod pageMethod = uriTemplate.getPageMethod();
        render(pageMethod.getFile(), pageMethod.getMethod().getName(), ParamBuilder.build(uriTemplate, inpurtParams), writer);
        return renderLayout(writer.toString());
    }

    /**
     * 渲染页面，如果有layout也在这里进行渲染
     *
     * @return
     * @throws IOException
     */
    public String render(File file, String method) throws Exception {
        StringWriter writer = new StringWriter();
        render(file, method, writer);
        return writer.toString();
    }

    public String renderTemplate(String name, Map<String, Object> context) {
        return velocityEngine.renderTemplate(name, context);
    }

    public void render(File groovyFile, String method, Writer writer) throws Exception {
        render(groovyFile, method, null, writer);
    }

    public void render(File groovyFile, String method, Object[] params, Writer writer) throws Exception {
        Context context = ThreadContext.getContext();
        try {
            Profiler.enter("execute groovy");
            context.setCurrentPath(DirectoryUtil.getFileParentPath(groovyFile));
            Object obj = groovyEngine.execute(groovyFile, method, params);
            if (obj instanceof CharSequence) {
                writer.write(obj.toString());
                return;
            }
        } finally {
            Profiler.release();
        }

        velocityEngine.renderTemplate(getTemplateName(groovyFile), context.getContextMap(), writer);
    }

    private String getTemplateName(File groovyFile) {
        String vmFile = DirectoryUtil.getFilePath(groovyFile).replace(".groovy", ".vm");
        return AppVmFile.getTemplateName(vmFile);
    }

    private String renderLayout(String screenPlaceholder) {
        Context context = ThreadContext.getContext();
        String layout = context.getLayout();
        App old = context.getApp();
        String layoutFilePath = getLayoutFilePath(context, layout);
        if (layoutFilePath != null) {
            try {
                String layoutFile = AppVmFile.getTemplateName(layoutFilePath);
                context.putContext("screen_placeholder", screenPlaceholder);
                int i = layoutFilePath.lastIndexOf("/");
                context.setCurrentPath(layoutFilePath.substring(0, i));
                return velocityEngine.renderTemplate(layoutFile, context.getContextMap());
            } finally {
                context.setApp(old);
            }
        }

        return screenPlaceholder;
    }

    /**
     * 支持如 layout:layout.vm
     *
     * @param context
     * @param layout
     * @return
     */
    private String getLayoutFilePath(Context context, String layout) {
        if (layout == null || !layout.endsWith(".vm") || context.getCurrentPath() == null) {
            return null;
        }
        String path = null;
        int i = layout.indexOf(":");
        if (i > 0) {
            String appName = layout.substring(0, i);
            String file = layout.substring(i + 1);
            App app = AppContainer.getApp(appName);
            if (app == null) {
                return null;
            }
            path = app.getRootPath() + "/" + file;
            //切换当前上下文的app，AppVmLoader中会去拿具体的文件
            context.setApp(app);
        } else {
            path = DirectoryUtil.getDirectory(context.getCurrentPath(), layout);
        }
        return path;
    }

}
