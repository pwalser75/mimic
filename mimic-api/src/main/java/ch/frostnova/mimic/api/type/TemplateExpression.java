package ch.frostnova.mimic.api.type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Template expression with literals and placeholders (format: {placeholder-name}), example: <code>Hello {name}, nice to
 * {action} you</code>
 *
 * @author pwalser
 * @since 25.01.2018.
 */
public class TemplateExpression implements Comparable<TemplateExpression> {

    private final static Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]+)}");

    private final String template;
    private final List<String> keys = new LinkedList<>();
    private final int literalCharacterLength;

    public TemplateExpression(String template) {
        this.template = template != null ? template : "";

        // find keys
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(this.template);
        while (matcher.find()) {
            keys.add(matcher.group(1));
        }
        literalCharacterLength = process(new HashMap<>()).length();
    }

    /**
     * Return the keys used in the template.<br>
     * Example: for <code>A string {with} some {place}holders</code>, return <code>with,place</code>
     *
     * @return set of placeholders (never null).
     */
    public Set<String> getPlaceholderKeys() {
        return new HashSet<>(keys);
    }

    /**
     * Checks if the template expression matches the given string (this is the case when for each key, there is a
     * value in the actual string, and replacing the keys with the responding values in the template will result in the
     * actual string).
     *
     * @param string string (can be null)
     * @return true when matching
     */
    public boolean matches(String string) {
        return getPlaceholderValues(string) != null;
    }

    /**
     * Processes the template by replacing all occurrence of the replacement keys with the mapped values.
     * Keys for which there is no replacement will be replaced with empty strings.
     *
     * @param replacements replacements, can be null.
     * @return string with replaced placeholders.
     */
    public String process(Map<String, String> replacements) {

        String result = template;
        for (String key : getPlaceholderKeys()) {
            result = result.replace("{" + key + "}", replacements.getOrDefault(key, ""));
        }
        return result;
    }

    /**
     * Match the template against the provided string and find and return the key/values for the template
     * placeholders.<br>
     * Example: for <code>The {what} today is {how}</code> and <code>The weather today is terrific</code>, return
     * <code>what=weather,how=terrific</code>
     *
     * @param string string, can be null
     * @return map of placeholder keys (from template) and their respective values (from actual string). When the
     * template and actual string don't match, null is returned.
     */
    public Map<String, String> getPlaceholderValues(String string) {
        if (string == null) {
            return null;
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuilder builder = new StringBuilder();
        int offset = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            builder.append(Pattern.quote(template.substring(offset, start)));
            builder.append("(.+)");
            offset = end;
        }
        if (offset < template.length()) {
            builder.append(Pattern.quote(template.substring(offset, template.length())));
        }

        matcher = Pattern.compile(builder.toString()).matcher(string);
        if (!matcher.matches()) {
            return null;
        }

        if (matcher.groupCount() != keys.size()) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = matcher.group(i + 1);
            if (result.containsKey(key) && !result.get(key).equalsIgnoreCase(value)) {
                return null; // discrepancy: key used twice, with different values
            }
            result.put(key, value);
        }

        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(template=" + template + ", keys=[" + keys.stream().distinct().sorted().collect(Collectors.joining(",")) + "], literal length=" + literalCharacterLength + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(), template);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return Objects.equals(template, ((TemplateExpression) obj).template);
    }

    /**
     * Order as specified in JSR-339 (JAX-RS): by number of literals (desc), then by number of placeholders (desc). <br>
     * Or in short: most specific wins.
     */
    @Override
    public int compareTo(TemplateExpression other) {
        if (other == null) {
            return -1;
        }
        int order = Integer.compare(literalCharacterLength, other.literalCharacterLength);
        if (order == 0) {
            order = Integer.compare(keys.size(), other.keys.size());
        }
        return -order;
    }
}
