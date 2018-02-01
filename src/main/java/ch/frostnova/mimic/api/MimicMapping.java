package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;

/**
 * A mimic rule, with method/path mapping and javascript script (ECMASCRIPT 5.1) for processing requests.
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public class MimicMapping {

    private Long id;
    private RequestMethod method;
    private String path;
    private String script;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
