package ch.frostnova.mimic.service;

import ch.frostnova.mimic.api.StaticResource;
import ch.frostnova.mimic.api.service.StaticResourceService;
import ch.frostnova.mimic.persistence.entity.StorageEntity;
import ch.frostnova.mimic.persistence.repository.StorageRepository;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckString;
import ch.frostnova.util.check.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;

/**
 * Implementation of the {@link StaticResourceService}.
 *
 * @author pwalser
 * @since 28.08.2018
 */
@Service
@EnableTransactionManagement
@Transactional
public class StaticResourceServiceImpl implements StaticResourceService {

    @Autowired
    private StorageRepository storageRepository;

    @Override
    public StaticResource get(String repositoryId, String name) {
        Check.required(repositoryId, "repositoryId", CheckString.notBlank());
        Check.required(name, "name", CheckString.notBlank());
        return storageRepository.findByRepositoryIdAndResourceId(repositoryId, name).map(this::convert).orElse(null);
    }

    @Override
    public StaticResource load(String resourceId) {
        Check.required(resourceId, "resourceId", CheckString.notBlank());
        return storageRepository.findById(resourceId).map(this::convert).orElse(null);
    }

    @Override
    public StaticResource save(StaticResource resource) {
        Check.required(resource, "resource");
        Check.required(resource.getRepositoryId(), "resource.repositoryId");
        Check.required(resource.getName(), "resource.name");
        Check.required(resource.getContent(), "resource.content", Verify.that(x -> x.length > 0, "not empty"));

        StorageEntity storageEntity;
        if (resource.getId() != null) {
            storageEntity = storageRepository.findById(resource.getId()).orElseThrow(NotFoundException::new);
        } else {
            storageEntity = storageRepository.findByRepositoryIdAndResourceId(resource.getRepositoryId(), resource.getName()).orElseGet(StorageEntity::new);
        }
        storageEntity = storageRepository.save(update(storageEntity, resource));
        return convert(storageEntity);
    }

    @Override
    public void delete(String resourceId) {
        Check.required(resourceId, "resourceId", CheckString.notBlank());
        storageRepository.findById(resourceId).ifPresent(storageRepository::delete);
    }

    private StorageEntity update(StorageEntity entity, StaticResource resource) {
        if (entity == null) {
            entity = new StorageEntity();
        }
        entity.setRepositoryId(resource.getRepositoryId());
        entity.setResourceId(resource.getName());
        entity.setContentType(resource.getContentType());
        entity.setContent(resource.getContent());
        entity.setContentLength(resource.getContent() != null ? resource.getContent().length : 0);
        return entity;

    }

    private StaticResource convert(StorageEntity entity) {
        if (entity == null) {
            return null;
        }
        StaticResource resource = new StaticResource();
        resource.setId(entity.getId());
        resource.setRepositoryId(entity.getRepositoryId());
        resource.setName(entity.getResourceId());
        resource.setContentType(entity.getContentType());
        resource.setContent(entity.getContent());
        return resource;
    }
}
