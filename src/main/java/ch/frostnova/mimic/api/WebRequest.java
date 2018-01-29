package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.converter.KeyValueStoreConverter;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import ch.frostnova.mimic.util.JsonUtil;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

/**
 * Model for a web request (HTTP)
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@XmlRootElement
public interface WebRequest {

    /**
     * Binds a path mapping to the request (defines placeholders for path parameters, such as
     * <code>/{tenant}/resource/{id}</code>
     *
     * @param pathMapping path mapping, optional (if not set, no path parameters will be extracted).
     */
    void bind(TemplateExpression pathMapping);

    @XmlElement(name = "session")
    @XmlJavaTypeAdapter(KeyValueStoreConverter.class)
    KeyValueStore getSession();

    @XmlElement(name = "method")
    RequestMethod getMethod();

    @XmlElement(name = "path")
    String getPath();

    @XmlElement(name = "headers")
    Map<String, String> getHeaders();

    @XmlElement(name = "pathParams")
    Map<String, String> getPathParams();

    @XmlElement(name = "queryParams")
    Map<String, String> getQueryParams();

    @XmlElement(name = "formParams")
    Map<String, String> getFormParams();

    @XmlElement(name = "body")
    String getBody();

    default String toJSON() {
        return JsonUtil.stringify(this);
    }
}
