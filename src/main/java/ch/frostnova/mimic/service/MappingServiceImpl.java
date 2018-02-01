package ch.frostnova.mimic.service;

import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.service.MappingService;
import ch.frostnova.mimic.persistence.MimicMappingEntity;
import ch.frostnova.mimic.persistence.MimicMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@EnableTransactionManagement
@Transactional(readOnly = true)
/**
 * Implementation of the MappingService
 */
public class MappingServiceImpl implements MappingService {

    @Autowired
    private MimicMappingRepository repository;

    @Override
    public MimicMapping get(long id) {
        return convert(repository.findOne(id));
    }

    @Override
    @Transactional(readOnly = false)
    public MimicMapping save(MimicMapping mapping) {
        MimicMappingEntity entity = new MimicMappingEntity();
        if (mapping.getId() != null) {
            entity = repository.findOne(mapping.getId());
        }
        return convert(repository.save(update(entity, mapping)));
    }

    @Override
    public List<MimicMapping> list() {
        Spliterator<MimicMappingEntity> spliterator = repository.findAll().spliterator();
        Stream<MimicMappingEntity> stream = StreamSupport.stream(spliterator, false);
        return stream.map(this::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(long id) {
        if (repository.exists(id)) {
            repository.delete(id);
        }
    }

    private MimicMapping convert(MimicMappingEntity entity) {
        if (entity == null) {
            return null;
        }
        MimicMapping dto = new MimicMapping();
        dto.setId(entity.getId());
        dto.setPath(entity.getPath());
        return dto;
    }

    private MimicMappingEntity update(MimicMappingEntity entity, MimicMapping dto) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setPath(dto.getPath());
        entity.setRequestMethod(dto.getMethod());
        entity.setScript(dto.getScript());
        return entity;
    }
}
