package org.easyweb.request.render;

import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.velocity.AppVmFile;
import org.easyweb.velocity.VelocityEngine;
import org.easyweb.app.App;
import org.easyweb.app.AppContainer;
import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.velocity.AppVmFile;
import org.easyweb.velocity.VelocityEngine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-2 下午2:11
 */
@Component
public class LayoutRender {
    @Resource
    VelocityEngine velocityEngine;

    public String render(String screenPlaceholder) {
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
