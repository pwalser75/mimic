package ch.frostnova.mimic.engine;

import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.WebRequest;
import ch.frostnova.mimic.api.WebResponse;
import ch.frostnova.mimic.io.ScriptsLoader;
import ch.frostnova.mimic.util.JsonUtil;
import ch.frostnova.util.check.Check;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Rule engine for processing requests.
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@Component
public class MimicEngine {

    @Autowired
    private Logger logger;


    @Autowired
    private ScriptsLoader mappingsLoader;

    private Set<MimicMapping> mappings = new HashSet<>();

    @PostConstruct
    private void init() throws IOException {
        logger.info(getClass().getName() + " init");
        mappings = mappingsLoader.loadMappings();
    }

    //TODO: try to reuse engine (in a thread-save manner)
    //TODO: see https://stackoverflow.com/questions/27710407/reuse-nashorn-scriptengine-in-servlet

    public void register(MimicMapping mapping) {
        Check.required(mapping, "mapping");

        // TODO: check for path name + method clashes
        mappings.add(mapping);
    }

    public void unregister(MimicMapping mapping) {
        if (mapping != null) {
            mappings.remove(mapping);
        }
    }

    public Set<MimicMapping> getMappings() {
        return Collections.unmodifiableSet(mappings);
    }

    public WebResponse eval(WebRequest request) {
        Check.required(request, "request");

        // find best matching mapping
        MimicMapping rule = resolveMapping(request);
        if (rule == null) {
            return WebResponse.error(404, "MIMIC: no matching rule found for " + request.getMethod() + " " + request.getPath());
        }
        request.bind(rule.getPathTemplate());

        try {
            WebResponse response = new WebResponse();
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval("var request = JSON.parse('" + JsonUtil.stringify(request) + "');");
            engine.put("response", response);
            engine.eval(rule.getCode());
            return response;
        } catch (ScriptException ex) {
            String errorMessage = "MIMIC: script evaluation failed:\n" + ex.getColumnNumber() + ":" + ex.getLineNumber() + ": " + ex.getMessage();
            logger.error(errorMessage);
            return WebResponse.error(500, errorMessage);
        } catch (Exception ex) {
            String errorMessage = "MIMIC: internal error (" + ex.getClass().getName() + ": " + ex.getMessage() + ")";
            logger.error(errorMessage);
            return WebResponse.error(500, errorMessage);
        }
    }

    private MimicMapping resolveMapping(WebRequest request) {
        return mappings.stream()
                .filter(m -> request.getMethod() == m.getMethod())
                .filter(m -> m.getPathTemplate().matches(request.getPath()))
                .sorted(Comparator.comparing(m -> m.getPathTemplate())).findFirst().orElse(null);
    }
}
