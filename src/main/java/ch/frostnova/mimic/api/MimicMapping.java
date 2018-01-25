package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import ch.frostnova.util.check.Check;

/**
 * A mimic rule, with method/path mapping and javascript code (ECMASCRIPT 5.1) for processing requests.
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public class MimicMapping {

    private RequestMethod method;
    private String path;
    private TemplateExpression pathTemplate;
    private String code;

    /**
     * Create a rule for a request method and path, processed by the given processing code.
     *
     * @param method request method
     * @param path   path template string, can contain placeholders for path parameters, e.g.
     *               "book/{isbn}/page{pageNumber}"
     * @param code   Javascript (ECMASCRIPT 5.1) code which processes the request
     */
    public MimicMapping(RequestMethod method, String path, String code) {
        this.method = Check.required(method, "method");
        this.path = Check.required(path, "path");
        pathTemplate = new TemplateExpression(path);
        this.code = Check.required(code, "code");
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public TemplateExpression getPathTemplate() {
        return pathTemplate;
    }

    public String getCode() {
        return code;
    }

    //TODO: equals and hash code
}
