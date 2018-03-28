package ch.frostnova.mimic.service.strategy;

import ch.frostnova.mimic.api.MappingProvider;
import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Built-in mapping provider
 *
 * @author pwalser
 * @since 31.01.2018.
 */
@Component
public class BuiltinMappingProvider implements MappingProvider, InitializingBean {

    private final Set<MimicMapping> mappings = new HashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {

        String banner = getBanner().replace("\\", "\\\\").replace("'", "\\'").replace("\n", "\\n");

        MimicMapping mapping = new MimicMapping();
        mapping.setDisplayName("Mimic Banner");
        mapping.setDescription("Built-in request mapping");
        mapping.setMethod(RequestMethod.GET);
        mapping.setPath("/");
        mapping.setScript("response.setStatus(200);\n" +
                "response.setContentType('text/plain');\n" +
                "response.setBody('" + banner + "');");
        mappings.add(mapping);
    }

    private String getBanner() throws IOException {

        URL bannerURL = getClass().getResource("/banner.txt");
        if (bannerURL == null) {
            return "MIMIC WEB SERVER | https://github.com/pwalser75/mimic";
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(bannerURL.openStream()))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }

    @Override
    public Set<MimicMapping> getMappings(RequestMethod method, String path) {
        return mappings.stream()
                .filter(m -> method == m.getMethod())
                .filter(m -> new TemplateExpression(m.getPath()).matches(path))
                .collect(Collectors.toSet());
    }
}
