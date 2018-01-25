package ch.frostnova.mimic.engine;

import ch.frostnova.mimic.api.MimicRule;
import ch.frostnova.mimic.api.WebRequest;
import ch.frostnova.mimic.api.WebResponse;
import ch.frostnova.mimic.util.JsonUtil;
import ch.frostnova.util.check.Check;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Rule engine for processing requests.
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public class RuleEngine {

    private Set<MimicRule> rules = new HashSet<>();

    //TODO: try to reuse engine (in a thread-save manner)
    //TODO: see https://stackoverflow.com/questions/27710407/reuse-nashorn-scriptengine-in-servlet


    public void registerRule(MimicRule rule) {
        Check.required(rule, "rule");

        // TODO: check for path name + method clashes
        rules.add(rule);
    }

    public void unregisterRule(MimicRule rule) {
        if (rule != null) {
            rules.remove(rule);
        }
    }

    public Set<MimicRule> getRules() {
        return Collections.unmodifiableSet(rules);
    }

    public WebResponse eval(WebRequest request) {
        Check.required(request, "request");

        // find matching rule
        MimicRule rule = findMostSpecificRule(request);
        if (rule == null) {
            return WebResponse.error(404, "MIMIC: no matching rule found for " + request.getMethod() + " " + request.getPath());
        }

        try {
            WebResponse response = new WebResponse();
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval("var request = JSON.parse('" + JsonUtil.stringify(request) + "');");
            engine.put("response", response);
            engine.eval(rule.getCode());
            return response;
        } catch (ScriptException ex) {
            //TODO: log error
            return WebResponse.error(500, "MIMIC: script evaluation failed:\n" + ex.getColumnNumber() + ":" + ex.getLineNumber() + ": " + ex.getMessage());
        } catch (Exception ex) {
            return WebResponse.error(500, "MIMIC: internal error (" + ex.getClass().getName() + ": " + ex.getMessage() + ")");
        }
    }

    private MimicRule findMostSpecificRule(WebRequest request) {

        //TODO: using mock rule

        if (request.getPath().startsWith("/debug")) {
            return new MimicRule(request.getMethod(), "/debug", "response.setStatus(200);" +
                    "response.setContentType('application/json');\n" +
                    "response.setBody(JSON.stringify(request));");
        }

        return new MimicRule(request.getMethod(), "/hello", "response.setStatus(200);\n" +
                "response.setContentType('application/xml');\n" +
                "response.setBody('<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<test>" +
                "<request path=\"'+request.path+'\"/>" +
                "<message from=\"Mimic\">Mimic greets you!</message>" +
                "</test>" +
                "');");

    }
}
