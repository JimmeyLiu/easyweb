package org.easyweb.request.render;

import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.util.EasywebLogger;
import org.easyweb.velocity.AppVmFile;
import org.easyweb.velocity.VelocityEngine;
import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.velocity.AppVmFile;
import org.easyweb.velocity.VelocityEngine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component("ewControlTool")
public class ControlTool {

    @Resource(name = "ewVelocityEngine")
    private VelocityEngine velocityEngine;
    @Resource(name = "ewCodeRender")
    private CodeRender codeRender;

    public Render setTemplate(String vmFile) {
        return new Render(vmFile);
    }

    public class Render {
        private String vmFile;

        private Map<String, Object> params = new HashMap<String, Object>();

        public Render(String vmFile) {
            this.vmFile = vmFile;
        }

        public Render add(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public String toString() {
            Context context = ThreadContext.getContext();
            String oldCurrentPath = context.getCurrentPath();
            String vmFilePath = DirectoryUtil.getDirectory(context.getCurrentPath(), vmFile);
//            int i = filePath.indexOf(Configuration.getDeployPath());
//            if (i >= 0) {
//                name = filePath.substring(i + Configuration.getDeployPath().length());
//            } else {
//                name = filePath;
//            }
            String groovyFile = vmFilePath.replace(".vm", ".groovy");
            File file = new File(groovyFile);
            if (file.canRead()) {
                try {
//                context.setLayout(null);
                    return codeRender.render(file, "execute");
                } catch (Exception e) {
                    EasywebLogger.error("ControlTool Render Error", e);
                    return "<!-- control error " + e.getMessage() + "-->";
                } finally {
                    context.setCurrentPath(oldCurrentPath);//把当前目录会写回去
                }
            }
            Map<String, Object> map = new HashMap<String, Object>(context.getContextMap());
            map.putAll(params);
            return velocityEngine.renderTemplate(AppVmFile.getTemplateName(vmFilePath), map);
        }

    }


}
