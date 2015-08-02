package org.easyweb.request.exception;

import com.alibaba.fastjson.JSON;
import org.easyweb.Configuration;
import org.easyweb.app.App;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jimmey on 15-8-2.
 */
public abstract class ExceptionHandler {

    ExceptionType type;

    protected ExceptionHandler(ExceptionType type) {
        this.type = type;
    }

    /**
     * 处理异常信息
     *
     * @param e        如果是页面代码执行异常，则将异常暴露出去
     * @param response
     */
    public abstract void handle(Exception e, HttpServletResponse response);

    protected void response(HttpServletResponse response, String content) {
        try {
            if (response.getContentType() == null) {
                response.setContentType("text/html;charset=" + Configuration.getRequestCharset());
            }
            byte[] bytes = content.getBytes(Configuration.getRequestCharset());
            response.setContentLength(bytes.length);
            response.setHeader("Content-Language", "zh-CN");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception ignore) {

        }
    }

    protected String printAppInfo(App app) {
        StringBuilder sb = new StringBuilder();
//        sb.append("<table border=1>");
//        sb.append("<tr><th width=80>Name").append("</th><td width=200>").append(app.getName()).append("</td></tr>");
//        sb.append("<tr><th>Status").append("</th><td>").append(app.getStatus()).append("</td></tr>");
//        sb.append("</table>");
        sb.append("<pre>").append(JSON.toJSONString(app,true)).append("</pre>");
        return sb.toString();
    }
}
