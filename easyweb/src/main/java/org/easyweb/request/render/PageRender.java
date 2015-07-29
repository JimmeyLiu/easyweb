package org.easyweb.request.render;

import org.easyweb.request.PageMethod;
import org.easyweb.request.render.param.ParamBuilder;
import org.easyweb.request.uri.UriTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.Map;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午5:54
 * <p/>
 * 执行页面渲染操作的
 */
@Component
public class PageRender {

    @Resource
    CodeRender codeRender;

    public String render(UriTemplate uriTemplate, Map<String, Object> inpurtParams) throws Exception {
        StringWriter writer = new StringWriter();
        PageMethod pageMethod = uriTemplate.getPageMethod();
        codeRender.render(pageMethod.getFile(), pageMethod.getMethod().getName(), ParamBuilder.build(uriTemplate, inpurtParams), writer);
        return writer.toString();
    }

}
