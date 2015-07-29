package org.easyweb.request.render;

import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.velocity.AppVmFile;
import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.groovy.GroovyEngine;
import org.easyweb.profiler.Profiler;
import org.easyweb.velocity.AppVmFile;
import org.easyweb.velocity.VelocityEngine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * groovy代码执行入口
 *
 * @author jimmey
 */
@Component("ewCodeRender")
public class CodeRender {

    @Resource(name = "ewGroovyEngine")
    private GroovyEngine groovyEngine;
    @Resource(name = "ewVelocityEngine")
    private VelocityEngine velocityEngine;

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
        }finally {
            Profiler.release();
        }

        velocityEngine.renderTemplate(getTemplateName(groovyFile), context.getContextMap(), writer);
    }

    private String getTemplateName(File groovyFile) {
        String vmFile = DirectoryUtil.getFilePath(groovyFile).replace(".groovy", ".vm");
        return AppVmFile.getTemplateName(vmFile);
    }

}
