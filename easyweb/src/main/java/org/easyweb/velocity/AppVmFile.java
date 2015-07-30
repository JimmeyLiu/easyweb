package org.easyweb.velocity;

import org.easyweb.Configuration;
import org.easyweb.util.DirectoryUtil;

import java.io.File;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午4:54
 * <p/>
 * 根据当前上下文App及groovy文件、vm文件获取vm文件名
 */
public class AppVmFile {

    /**
     * 传入的是完整的vm路径，这里做替换
     *
     * @param vmFile
     * @return
     */
    public static String getTemplateName(File vmFile) {
        return getTemplateName(DirectoryUtil.getFilePath(vmFile));
    }

    public static String getTemplateName(String vmFile) {
        return vmFile.replace(getAppVmRoot(), "");
    }

//    /**
//     * 获取app的根目录
//     *
//     * @return
//     */
//    public static String getAppRootPath() {
//        App app = ThreadContext.getContext().getApp();
//        if (app == null) {
//            return DirectoryUtil.normalizePath(Configuration.getDeployPath());
//        } else {
//            return DirectoryUtil.normalizePath(app.getRootPath());
//        }
//    }

    static String appVmRoot = null;

    public static String getAppVmRoot() {
        if (appVmRoot != null) {
            return appVmRoot;
        }
        appVmRoot = DirectoryUtil.normalizePath(Configuration.getDeployPath());
        return appVmRoot;
    }


}
