package ch.frostnova.mimic.service.engine;

import ch.frostnova.mimic.api.*;
import ch.frostnova.mimic.api.service.StaticResourceService;
import ch.frostnova.mimic.api.type.TemplateExpression;
import ch.frostnova.util.check.Check;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Engine for processing requests using Mimic mappings
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MimicEngine {

    @Autowired
    private Logger logger;

    @Autowired
    private List<MappingProvider> mappingProviders;

    @Autowired
    private StaticResourceService staticResourceService;

    @PostConstruct
    public void init() {
        logger.info("Initialized with providers: " +
                mappingProviders.stream()
                        .map(Object::getClass).map(Class::getSimpleName)
                        .collect(Collectors.joining(", ")));
    }

    public WebResponse eval(WebRequest request) {
        Check.required(request, "request");

        // find best matching mapping
        Optional<MimicMapping> mapping = resolveMapping(request);
        if (!mapping.isPresent()) {
            return WebResponse.error(404, "MIMIC: no matching mapping found for " + request.getMethod() + " " + request.getPath());
        }
        request.bind(new TemplateExpression(mapping.get().getPath()));

        try {
            WebResponse response = new WebResponse();
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.put("request", request);
            engine.put("response", response);
            engine.put("repository", new Repository(staticResourceService));
            engine.eval(mapping.get().getScript());
            return response;
        } catch (ScriptException ex) {
            String errorMessage = "MIMIC: script evaluation failed:\n" + ex.getColumnNumber() + ":" + ex.getLineNumber() + ": " + ex.getMessage();
            logger.error(errorMessage);
            return WebResponse.error(500, errorMessage);
        } catch (Exception ex) {
            String errorMessage = "MIMIC: internal error (" + ex.getClass().getName() + ": " + ex.getMessage() + ")";
            logger.error(errorMessage, ex);
            return WebResponse.error(500, errorMessage);
        }
    }

    private Optional<MimicMapping> resolveMapping(WebRequest request) {

        List<MimicMapping> mimicMappings = new ArrayList<>();
        for (MappingProvider provider : mappingProviders) {
            mimicMappings.addAll(provider.getMappings(request.getMethod(), request.getPath()));
        }

        return mimicMappings.stream()
                .filter(m -> request.getMethod() == m.getMethod())
                .filter(m -> new TemplateExpression(m.getPath()).matches(request.getPath()))
                .sorted(Comparator.comparing(m -> new TemplateExpression(m.getPath()))).findFirst();

    }
}
