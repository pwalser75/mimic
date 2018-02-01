package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A mimic rule, with method/path mapping and javascript script (ECMASCRIPT 5.1) for processing requests.
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@XmlRootElement
public class MimicMapping {

    private Long id;
    private RequestMethod method;
    private String path;
    private String script;

    @XmlElement(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement(name = "method")
    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    @XmlElement(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @XmlElement(name = "script")
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
