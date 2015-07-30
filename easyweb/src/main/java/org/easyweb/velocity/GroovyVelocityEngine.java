    package org.easyweb.velocity;

    import javax.annotation.Resource;

    import org.easyweb.context.Context;
    import org.easyweb.context.ThreadContext;
    import org.easyweb.util.DirectoryUtil;
    import org.springframework.stereotype.Component;

    /**
     * Created with IntelliJ IDEA. User: jimmey Date: 12-11-23 Time: 下午11:11 To
     * change this template use File | Settings | File Templates.
     */
    @Component("ewGroovyVelocityEngine")
    public class GroovyVelocityEngine {

        @Resource(name = "ewVelocityEngine")
        private VelocityEngine velocityEngine;

        public String render(String templateName) {
            Context context = ThreadContext.getContext();
            String name = DirectoryUtil.getDirectory(context.getCurrentPath(), templateName);
            return velocityEngine.renderTemplate(AppVmFile.getTemplateName(name), context.getContextMap());
        }

    }
