package org.easyweb.velocity;

import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.request.render.CodeRender;
import org.easyweb.util.DirectoryUtil;

/**
 * Created with IntelliJ IDEA. User: jimmey Date: 12-11-23 Time: 下午11:11 To
 * change this template use File | Settings | File Templates.
 */
public class Vm {

    public String render(String templateName) {
        Context context = ThreadContext.getContext();
        String name = DirectoryUtil.getDirectory(context.getCurrentPath(), templateName);
        return CodeRender.getInstance().renderTemplate(AppVmFile.getTemplateName(name), context.getContextMap());
    }

}
