package org.easyweb.velocity;

import org.easyweb.Configuration;
import org.easyweb.context.ThreadContext;
import org.easyweb.velocity.tool.PageAttributeTool;
import org.easyweb.velocity.tool.SystemUtil;
import org.easyweb.util.EasywebLogger;
import org.easyweb.profiler.Profiler;
import org.easyweb.request.render.ControlTool;
import org.easyweb.velocity.event.EscapeHtmlEvent;
import org.easyweb.velocity.event.IgnoreTool;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA. User: jimmey Date: 12-11-23 Time: 下午10:01 To
 * change this template use File | Settings | File Templates.
 */
@Component("ewVelocityEngine")
public class VelocityEngine implements InitializingBean {


    private org.apache.velocity.app.VelocityEngine velocityEngine;
    //    @Resource(name = "ewCommonTool")
//    private CommonTool commonTool;
    @Resource(name = "ewControlTool")
    private ControlTool controlTool;
    @Resource
    private Map<String, Object> velocityTools;
    private PageAttributeTool page = new PageAttributeTool();

    private static Map<String, Object> staticTools = new HashMap<String, Object>();

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * directive.set.null.allowed=true input.encoding=GBK
         * output.encoding=GBK resource.loader = file file.resource.loader.class
         * = org.apache.velocity.runtime.resource.loader.FileResourceLoader
         * file.resource.loader.path =/home/admin/easyweb/components
         * file.resource.loader.cache = true
         * file.resource.loader.modificationCheckInterval = 2
         */
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("directive.set.null.allowed", "true");
        velocityProperties.setProperty("input.encoding", Configuration.getCodeCharset());
        velocityProperties.setProperty("output.encoding", Configuration.getCodeCharset());
        velocityProperties.setProperty("resource.loader", "appFile,string");
//        velocityProperties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//        velocityProperties.setProperty("file.resource.loader.path", Configuration.getDeployPath());
        velocityProperties.setProperty("appFile.resource.loader.class", "org.easyweb.velocity.AppVmLoader");
        velocityProperties.setProperty("appFile.resource.loader.cache", "true");
        velocityProperties.setProperty("appFile.resource.loader.modificationCheckInterval", "3");
        velocityProperties.setProperty("eventhandler.referenceinsertion.class", "org.easyweb.velocity.event.EscapeHtmlEvent");

        velocityProperties.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");

        velocityEngine = new org.apache.velocity.app.VelocityEngine();
        velocityEngine.init(velocityProperties);
        if (velocityTools == null) {
            velocityTools = new HashMap<String, Object>();
        }
//        velocityTools.put("commonTool", commonTool);
        velocityTools.put("controlTool", controlTool);
        velocityTools.put("assetsTool", page);
        velocityTools.put("pageTool", page);
        velocityTools.put("systemUtil", new SystemUtil());
        velocityTools.put("ignoreTool", new IgnoreTool());
    }

    public static void addTool(String toolName, Object object) {
        if (staticTools.containsKey(toolName)) {
            return;
        }
        staticTools.put(toolName, object);
        EscapeHtmlEvent.addNoescape("^" + toolName + "\\.");
    }

    /**
     * @param name    需要从 Configuration.getDeployPath()下开始，这样才能保持名称的唯一
     * @param context
     * @return
     */
    public String renderTemplate(String name, Map<String, Object> context) {
        StringWriter writer = new StringWriter();
        renderTemplate(name, context, writer);
        return writer.toString();
    }

    public void renderTemplate(String name, Map<String, Object> context, Writer writer) {
        Profiler.enter("render vm " + name);
        VelocityContext velocityContext = new VelocityContext(context);
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }
        // velocityContext.put("codeInclude", codeInclude);
        if (velocityTools != null) {
            for (Map.Entry<String, Object> entry : velocityTools.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }
        }
        if (!staticTools.isEmpty()) {
            for (Map.Entry<String, Object> entry : staticTools.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }
        }
        Map<String, Object> appTools = VmToolFactory.getAppTools(ThreadContext.getContext().getAppName());
        if (appTools != null) {
            for (Map.Entry<String, Object> entry : appTools.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }
        }

        try {
            velocityEngine.getTemplate(name).merge(velocityContext, writer);
        } catch (Exception e) {
            EasywebLogger.error("velocity error", e);
        } finally {
            Profiler.release();
        }
    }

    public String renderTemplate(String name, String code, Map<String, Object> context) {
        StringResourceRepository vsRepository = StringResourceLoader.getRepository();
        vsRepository.putStringResource(name, code, Configuration.getCodeCharset());
        StringWriter writer = new StringWriter();
        renderTemplate(name, context, writer);
        return writer.toString();
    }

}
