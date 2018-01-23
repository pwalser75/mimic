package ch.frostnova.mimic.rule;

import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.util.check.Check;

/**
 * @author pwalser
 * @since 23.01.2018.
 */
public class MimicRule {

    private RequestMethod method;
    private String path;
    private String code;

    /**
     * Create a rule for a request method and path, processed by the given processing code.
     *
     * @param method request method
     * @param path   requested path. Can contain placeholders for path parameters, e.g. "book/{isbn}/page{pageNumber}"
     * @param code   Javascript (ECMASCRIPT 5.1) code which processes the request
     */
    public MimicRule(RequestMethod method, String path, String code) {
        this.method = Check.required(method, "method");
        this.path = Check.required(path, "path");
        this.code = Check.required(code, "code");
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getCode() {
        return code;
    }

    //TODO: equals and hash code
}
