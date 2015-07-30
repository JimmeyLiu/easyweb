package org.easyweb.util;

import org.easyweb.Configuration;
import org.easyweb.velocity.tool.SystemUtil;
import org.easyweb.Configuration;
import org.easyweb.velocity.tool.SystemUtil;

import java.io.File;

/**
 * 相对路径处理
 *
 * @author jimmey
 */
public class DirectoryUtil {

    public static String FILE_SPLIT = "/";

    private static boolean isWin = SystemUtil.getOsInfo().isWindows();

    /**
     * @param base           如com/taobao/cmsadmin
     * @param relativelyFile 如../backyard/index.vm
     * @return
     */
    public static String getDirectory(String base, String relativelyFile) {
        String[] basePath = base.split(FILE_SPLIT);
        String[] targetPath = relativelyFile.split(FILE_SPLIT);
        StringBuilder sb = new StringBuilder();
        int parentDepth = 0;
        for (String s : targetPath) {
            if (s.equals("..")) {
                parentDepth++;
            }
        }
        if (parentDepth >= basePath.length && parentDepth > 0) {
            // 超出了
            return null;
        }
        if (parentDepth == 0 && targetPath.length == 0) {
            sb.append(base).append(FILE_SPLIT).append(relativelyFile);
        } else {
            for (int i = 0; i < basePath.length - parentDepth; i++) {
                sb.append(basePath[i]).append(FILE_SPLIT);
            }
            for (int i = parentDepth; i < targetPath.length; i++) {
                if (!".".equals(targetPath[i]) && !"".equals(targetPath[i])) {
                    sb.append(targetPath[i]);
                    if (i != targetPath.length - 1) {
                        sb.append(FILE_SPLIT);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 解决文件路径在不同操作系统下的问题，全部用unix风格
     *
     * @param file
     * @return
     */
    public static String getFilePath(File file) {
        return normalizePath(file.getAbsolutePath());
    }

    public static String getFileParentPath(File file) {
        return normalizePath(file.getParent());
    }


    public static String getAppDeployPath(String appName) {
        return Configuration.getDeployPath();
    }

    public static String getAppTempDeployPath(String appName) {
        return Configuration.getDeployPath();
    }

    public static String getAppDeployFile(String appName, String fileName) {
        return getAppDeployPath(appName) + fileName;
    }

    public static String getAppTemplateDeployFile(String appName, String fileName) {
        return getAppTempDeployPath(appName) + fileName;
    }

    public static String getDevPath(String appName, String fileName) {
        return "";
    }


    public static void main(String[] args) {
        System.out.println(normalizePath("D:\\a\\b"));
    }

    public static final String normalizePath(String path) {
        // Normalize the slashes and add leading slash if necessary
        String normalized = path;
        if (normalized.indexOf('\\') >= 0) {
            normalized = normalized.replace('\\', '/');
        }

        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // Resolve occurrences of "%20" in the normalized path
        while (true) {
            int index = normalized.indexOf("%20");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) + " " +
                    normalized.substring(index + 3);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
        }

        //windows操作系统盘符全部用小写开始
        if (isWin) {
            char c = normalized.charAt(0);
            if (Character.isUpperCase(c)) {
                normalized = Character.toLowerCase(c) + normalized.substring(1);
            }
        }

        return (normalized);
    }

}
