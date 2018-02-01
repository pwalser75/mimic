package ch.frostnova.mimic.api.service;

import ch.frostnova.mimic.api.MimicMapping;

import java.util.List;

/**
 * Mimic mapping service
 */
public interface MappingService {

    MimicMapping get(long id);

    MimicMapping save(MimicMapping mapping);

    List<MimicMapping> list();

    void delete(long id);

}
