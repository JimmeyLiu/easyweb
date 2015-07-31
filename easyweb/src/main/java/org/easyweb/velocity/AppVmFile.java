package org.easyweb.velocity;

import org.easyweb.context.ThreadContext;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午4:54
 * <p/>
 * 根据当前上下文App及groovy文件、vm文件获取vm文件名
 */
public class AppVmFile {

    public static String getTemplateName(String vmFile) {
        return vmFile.replace(getAppVmRoot(), "");
    }

    public static String getAppVmRoot() {
        return ThreadContext.getApp().getRootParent();
    }


}
