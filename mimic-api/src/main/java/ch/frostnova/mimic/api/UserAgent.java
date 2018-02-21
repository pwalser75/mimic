package ch.frostnova.mimic.api;

import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User agent object, can extract user agent name, version and OS from a given user agent string.
 *
 * @author pwalser
 * @since 08.02.2018.
 */
public class UserAgent {

    public final static String UNKNOWN = "?";

    private final static Pattern FIREFOX = Pattern.compile("(Firefox)\\/([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private final static Pattern SEAMONKEY = Pattern.compile("(Seamonkey)\\/([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private final static Pattern EDGE = Pattern.compile("(Edge)\\/([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private final static Pattern CHROME = Pattern.compile("(Chrome)\\/([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private final static Pattern CHROMIUM = Pattern.compile("(Chromium)\\/([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private final static Pattern OPERA = Pattern.compile("(Opera|OPR)\\/([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private final static Pattern SAFARI = Pattern.compile("(Safari)\\/([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private final static Pattern MSIE = Pattern.compile("(MSIE) ([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private final static Pattern OTHER = Pattern.compile("(\\w+)\\/([^\\s]+)", Pattern.CASE_INSENSITIVE);

    private final static Pattern[] DETECTION_ORDER = {OPERA, SEAMONKEY, EDGE, MSIE, CHROME, CHROMIUM, SAFARI, FIREFOX, OTHER};
    private final static Pattern OS_PATTERN = Pattern.compile("\\(([^;^\\)]+)", Pattern.CASE_INSENSITIVE);

    private final String operatingSystem;
    private final String userAgentName;
    private final String userAgentVersion;

    public UserAgent(String userAgentString) {

        Check.required(userAgentString, "userAgentString", CheckString.notBlank());

        Matcher osMatcher = OS_PATTERN.matcher(userAgentString);
        operatingSystem = osMatcher.find() ? osMatcher.group(1) : UNKNOWN;

        Matcher matcher = detectBrowser(userAgentString);
        userAgentName = matcher != null ? matcher.group(1) : UNKNOWN;
        userAgentVersion = matcher != null ? matcher.group(2) : UNKNOWN;
    }

    private static Matcher detectBrowser(String userAgentString) {
        for (Pattern p : DETECTION_ORDER) {
            Matcher matcher = p.matcher(userAgentString);
            if (matcher.find()) {
                return matcher;
            }
        }
        return null;
    }

    public String getOS() {
        return operatingSystem;
    }

    public String getName() {
        return userAgentName;
    }

    public String getVersion() {
        return userAgentVersion;
    }
}
