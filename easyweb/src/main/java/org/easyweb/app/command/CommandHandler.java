package org.easyweb.app.command;

import com.alibaba.fastjson.JSON;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.app.AppContainer;
import org.easyweb.app.AppStatus;
import org.easyweb.app.listener.AppChangeHolder;
import org.easyweb.app.monitor.AppMonitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jimmey on 15-8-1.
 */
public class CommandHandler {

    private static Pattern pattern = Pattern.compile("^/easyweb/(\\w+)/(\\w+)[/(\\w+)]?$");

    public static void main(String[] args) {
        String s = "/easyweb/demo/stop";
        Matcher m = pattern.matcher(s);
        if (m.matches()) {
            System.out.println(m.group(1));
            System.out.println(m.group(2));
        }
    }

    @SuppressWarnings("all")
    public static boolean handle(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        if (!uri.startsWith("/easyweb")) {
            return false;
        }
        Result result = new Result();
        Matcher m = pattern.matcher(uri);
        if (!m.matches()) {
            result.apps.addAll(AppContainer.getApps());
            printInfo(result, response);
            return true;
        }

        String appName = m.group(1);
        String cmd = m.group(2);
        String token = m.groupCount() == 3 ? m.group(3) : null;
        App app = AppContainer.getApp(appName);
        boolean tokenValidate = Configuration.getDeployToken().equals(token);
        if (tokenValidate) {
            if ("deploy".equals(cmd)) {
//                FileUploader.process(request);
//                byte[] body = FileUploader.getFileBytes(request);
//                try {
//                    String appsRoot = Configuration.getAppsRoot();
//                    if (app != null) {
//                        AppChangeHolder.stop(app);
//                        File lock = app.getLock();
//                        if (!lock.exists()) {
//                            try {
//                                lock.createNewFile();
//                            } catch (Exception e) {
//                            }
//                        }
//                        new File(app.getRootPath()).delete();
//                        appsRoot = app.getRootParent();
//                    }
//
//                    File file = new File(appsRoot, appName);
//                    FileOutputStream out = new FileOutputStream(file);
//                    IOUtils.copy(new ByteArrayInputStream(body), out);
//
//                } catch (Exception e) {
//                    result.setSuccess(false);
//                    result.setMessage("Upload Error " + e.getMessage());
//                }
            } else if ("start".equals(cmd)) {
                if (app != null && app.getStatus() == AppStatus.STOP) {
                    AppChangeHolder.success(app);
                }
                AppMonitor.getInstance().start();
            } else if ("stop".equals(cmd) && app != null) {
                AppChangeHolder.stop(app);
            }
        } else {
            result.setSuccess(false);
            result.setMessage("Token Error");
        }
        printInfo(result, response);
        return true;
    }

    private static void printInfo(Result result, HttpServletResponse response) {
        byte[] bytes = JSON.toJSONBytes(result);
        try {
            response.setContentLength(bytes.length);
            response.setHeader("Content-Language", "zh-CN");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception ignore) {
        }
    }

    public static class Result {
        boolean success = true;
        List<App> apps = new ArrayList<App>();
        String message;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<App> getApps() {
            return apps;
        }

        public void setApps(List<App> apps) {
            this.apps = apps;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
