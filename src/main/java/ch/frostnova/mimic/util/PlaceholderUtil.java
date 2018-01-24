package ch.frostnova.mimic.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for dealing with Strings with placeholders (such as "Hello {name}, nice to {action} you").
 *
 * @author pwalser
 * @since 24.01.2018.
 */
public final class PlaceholderUtil {

    private final static Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]+)}");

    private PlaceholderUtil() {

    }

    /**
     * For a given placeholder template, find and return the keys.<br>
     * Example: for <code>A string {with} some {place}holders</code>, return <code>with,place</code>
     *
     * @param placeholderTemplate string with placeholders, optional (can be null)
     * @return set of placeholders (never null).
     */
    public static Set<String> getPlaceholderKeys(String placeholderTemplate) {

        return new HashSet<>(listPlaceholderKeys(placeholderTemplate));
    }

    private static List<String> listPlaceholderKeys(String placeholderTemplate) {

        if (placeholderTemplate == null) {
            return Collections.emptyList();
        }
        List result = new LinkedList();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(placeholderTemplate);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    /**
     * Checks if the placeholder template matches the actual string (this is the case when for each key, there is a
     * value in the actual string, and replacing the keys with the responding values in the template will result in the
     * actual string).
     *
     * @param placeholderTemplate string with placeholders (can be null)
     * @param actualString        actual string (can be null
     * @return true when matching
     */
    public static boolean matches(String placeholderTemplate, String actualString) {
        return getPlaceholderValues(placeholderTemplate, actualString) != null;
    }

    /**
     * Replaces all occurrence of the replacement keys (in curly braces, such as <code>{key}</code>) in the placeholder
     * template with the mapped values. Placeholders for which there is no replacement will be replaced with empty
     * strings.
     *
     * @param placeholderTemplate string with placeholders (can be null)
     * @param replacements        replacements, can be null.
     * @return string with replaced placeholders.
     */
    public static String replace(String placeholderTemplate, Map<String, String> replacements) {

        for (String key : getPlaceholderKeys(placeholderTemplate)) {
            placeholderTemplate = placeholderTemplate.replace("{" + key + "}", replacements.getOrDefault(key, ""));
        }
        return placeholderTemplate;
    }

    /**
     * For a given string with placeholders, find and return the keys.<br>
     * Example: for <code>The {what} today is {how}</code> and <code>The weather today is terrific</code>, return
     * <code>what=weather,how=terrific</code>
     *
     * @param placeholderTemplate string with placeholders, optional (can be null)
     * @param actualString        actual string
     * @return map of placeholder keys (from template) and their respective values (from actual string). When the
     * template and actual string don't match, null is returned.
     */
    public static Map<String, String> getPlaceholderValues(String placeholderTemplate, String actualString) {
        if (actualString == null) {
            return null;
        }
        if (placeholderTemplate == null) {
            return Collections.emptyMap();
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(placeholderTemplate);
        StringBuilder builder = new StringBuilder();
        int offset = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            builder.append(Pattern.quote(placeholderTemplate.substring(offset, start)));
            builder.append("(.+)");
            offset = end;
        }
        if (offset < placeholderTemplate.length()) {
            builder.append(Pattern.quote(placeholderTemplate.substring(offset, placeholderTemplate.length())));
        }

        matcher = Pattern.compile(builder.toString()).matcher(actualString);
        if (!matcher.matches()) {
            return null;
        }

        List<String> keys = listPlaceholderKeys(placeholderTemplate);
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
}
