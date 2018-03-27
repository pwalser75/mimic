package ch.frostnova.mimic.service;

import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.service.MappingService;
import ch.frostnova.mimic.persistence.entity.MappingEntity;
import ch.frostnova.mimic.persistence.repository.MappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Implementation of the MappingService
 */
@Service
@EnableTransactionManagement
@Transactional
public class MappingServiceImpl implements MappingService {

    @Autowired
    private MappingRepository repository;

    @Override
    @Transactional(readOnly = true)
    public MimicMapping get(String id) {
        return convert(repository.findOne(id));
    }

    @Override
    public MimicMapping save(MimicMapping mapping) {
        MappingEntity entity = new MappingEntity();
        if (mapping.getId() != null) {
            entity = repository.findOne(mapping.getId());
        }
        return convert(repository.save(update(entity, mapping)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MimicMapping> list() {
        Spliterator<MappingEntity> spliterator = repository.findAll().spliterator();
        Stream<MappingEntity> stream = StreamSupport.stream(spliterator, false);
        return stream.map(this::convert).collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        if (repository.exists(id)) {
            repository.delete(id);
        }
    }

    private MimicMapping convert(MappingEntity entity) {
        if (entity == null) {
            return null;
        }
        MimicMapping dto = new MimicMapping();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setLastModifiedAt(entity.getLastModifiedAt());
        dto.setDisplayName(entity.getDisplayName());
        dto.setDescription(entity.getDescription());
        dto.setMethod(entity.getRequestMethod());
        dto.setPath(entity.getPath());
        dto.setScript(entity.getScript());
        return dto;
    }

    private MappingEntity update(MappingEntity entity, MimicMapping dto) {
        if (dto == null) {
            return null;
        }
        entity.setDisplayName(dto.getDisplayName());
        entity.setDescription(dto.getDescription());
        entity.setPath(dto.getPath());
        entity.setRequestMethod(dto.getMethod());
        entity.setScript(dto.getScript() != null ? dto.getScript().trim() : null);
        return entity;
    }
}
