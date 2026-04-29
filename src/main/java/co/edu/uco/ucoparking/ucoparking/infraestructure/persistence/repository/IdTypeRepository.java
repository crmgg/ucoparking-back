package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.repository;

import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.entity.IdTypeEntity;

import java.util.List;
import java.util.UUID;

public interface IdTypeRepository {

    void create(IdTypeEntity entity);

    void update(IdTypeEntity entity);

    void delete(IdTypeEntity entity);

    IdTypeEntity findById(UUID id);

    List<IdTypeEntity> findByFilter(IdTypeEntity entity);

    List<IdTypeEntity> findAll(IdTypeEntity entity);

}

