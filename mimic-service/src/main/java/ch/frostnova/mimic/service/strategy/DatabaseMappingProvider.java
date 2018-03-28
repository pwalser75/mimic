package ch.frostnova.mimic.service.strategy;

import ch.frostnova.mimic.api.MappingProvider;
import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import ch.frostnova.mimic.persistence.repository.MappingRepository;
import ch.frostnova.mimic.service.MappingServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapping provider reading mappings from a repository.
 *
 * @author pwalser
 * @since 28.03.2018.
 */
@Component
public class DatabaseMappingProvider implements MappingProvider {

    @Autowired
    private Logger logger;

    @Autowired
    private MappingRepository repository;

    @Override
    public Set<MimicMapping> getMappings(RequestMethod method, String path) {

        return repository.findByRequestMethod(method).stream()
                .filter(m -> new TemplateExpression(m.getPath()).matches(path))
                .map(MappingServiceImpl::convert)
                .collect(Collectors.toSet());
    }
}
