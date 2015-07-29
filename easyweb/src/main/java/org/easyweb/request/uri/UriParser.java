package org.easyweb.request.uri;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: jimmey/shantong
 * Date: 13-7-11
 * Time: 下午3:13
 */
public class UriParser {
    private final List<String> variableNames = new LinkedList<String>();

    private final StringBuilder patternBuilder = new StringBuilder();
    private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
    private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";

    private Pattern pattern;

    public boolean matches(String uri) {
        Matcher matcher = getMatchPattern().matcher(uri);
        return matcher.matches();
    }

    public Map<String, String> match(String uri) {
        Map<String, String> result = new LinkedHashMap<String, String>(this.variableNames.size());
        Matcher matcher = getMatchPattern().matcher(uri);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String name = this.variableNames.get(i - 1);
                String value = matcher.group(i);
                result.put(name, value);
            }
        }
        return result;
    }

    public UriParser(String uriTemplate) {
        Matcher m = NAMES_PATTERN.matcher(uriTemplate);
        int end = 0;
        while (m.find()) {
            this.patternBuilder.append(quote(uriTemplate, end, m.start()));
            String match = m.group(1);
            int colonIdx = match.indexOf(':');
            if (colonIdx == -1) {
                this.patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
                this.variableNames.add(match);
            } else {
                if (colonIdx + 1 == match.length()) {
                    throw new IllegalArgumentException("No custom regular expression specified after ':' in \"" + match + "\"");
                }
                String variablePattern = match.substring(colonIdx + 1, match.length());
                this.patternBuilder.append('(');
                this.patternBuilder.append(variablePattern);
                this.patternBuilder.append(')');
                String variableName = match.substring(0, colonIdx);
                this.variableNames.add(variableName);
            }
            end = m.end();
        }
        this.patternBuilder.append(quote(uriTemplate, end, uriTemplate.length()));
        int lastIdx = this.patternBuilder.length() - 1;
        if (lastIdx >= 0 && this.patternBuilder.charAt(lastIdx) == '/') {
            this.patternBuilder.deleteCharAt(lastIdx);
        }
    }

    private String quote(String fullPath, int start, int end) {
        if (start == end) {
            return "";
        }
        return Pattern.quote(fullPath.substring(start, end));
    }

    private List<String> getVariableNames() {
        return Collections.unmodifiableList(this.variableNames);
    }

    private Pattern getMatchPattern() {
        if (pattern == null) {
            this.pattern = Pattern.compile(this.patternBuilder.toString());
        }
        return this.pattern;
    }
}
