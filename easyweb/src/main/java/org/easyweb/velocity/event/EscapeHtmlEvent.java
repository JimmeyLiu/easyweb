package org.easyweb.velocity.event;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.runtime.Renderable;
import org.easyweb.app.App;
import org.easyweb.app.listener.AppChangeAdapter;
import org.easyweb.context.ThreadContext;
import org.easyweb.velocity.tool.StringEscapeUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: jimmey
 * Date: 13-5-30
 * Time: 下午10:56
 * To change this template use File | Settings | File Templates.
 */
public class EscapeHtmlEvent extends AppChangeAdapter implements ReferenceInsertionEventHandler {

    private static List<Pattern> noescapeList = new ArrayList<Pattern>(3);
    private static Set<String> keys = new HashSet<String>();

    static {
        noescapeList.add(Pattern.compile("^screen_placeholder"));
        noescapeList.add(Pattern.compile("^controlTool\\."));
        noescapeList.add(Pattern.compile("^pageTool\\."));
        noescapeList.add(Pattern.compile("^ignoreTool\\."));
        noescapeList.add(Pattern.compile("^systemUtil\\."));
    }

    public static void addNoescape(String pattern) {
        try {
            if (keys.contains(pattern)) {
                return;
            }
            keys.add(pattern);
            noescapeList.add(Pattern.compile(pattern));
        } catch (Exception e) {
        }
    }

    private static Map<String, List<Pattern>> appNoescapePattern = new HashMap<String, List<Pattern>>();

    @Override
    public void stop(App app) {
        appNoescapePattern.remove(app.getName());
    }


    @Override
    public void success(App app) {
        List<Pattern> patterns = new ArrayList<Pattern>(noescapeList);
        String rules = app.getConfig(App.VELOCITY_NO_ESCAPE);
        if (rules != null) {
            for (String rule : rules.split(",")) {
                patterns.add(Pattern.compile(rule));
            }
        }
        appNoescapePattern.put(app.getName(), patterns);
    }

    @Override
    public Object referenceInsert(String reference, Object value) {
        App app = ThreadContext.getContext().getApp();
        if (app == null || value == null) {
            return value;
        }
        String reference1 = normalizeReference(reference);
        if (reference == null) {
            return value;
        }

        List<Pattern> noescapes = appNoescapePattern.get(app.getName());
        if (noescapes == null) {
            return value;
        }
        for (Pattern p : noescapes) {
            if (p.matcher(reference1).find()) {
                return value;
            }
        }
        if (value instanceof Renderable) {
            return value;
        }
        return StringEscapeUtil.escapeHtml(value.toString());
    }

    private static final Pattern referencePattern = Pattern
            .compile("\\s*\\$\\s*\\!?\\s*(\\{\\s*(.*?)\\s*\\}|(.*?))\\s*");

    private static String normalizeReference(String reference) {
        if (reference == null) {
            return "";
        }

        Matcher matcher = referencePattern.matcher(reference);

        if (matcher.matches()) {
            String form1 = matcher.group(2);
            String form2 = matcher.group(3);

            if (form1 == null) {
                return form2;
            } else {
                return form1;
            }
        } else {
            return reference;
        }
    }
}
