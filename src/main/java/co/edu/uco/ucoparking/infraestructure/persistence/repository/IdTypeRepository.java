package co.edu.uco.ucoparking.infraestructure.persistence.repository;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.IdTypeEntity;

import java.util.List;
import java.util.UUID;

public interface IdTypeRepository {

    void create(IdTypeEntity entity);

    void update(IdTypeEntity entity);

    void delete(UUID id);

    IdTypeEntity findById(UUID id);

    List<IdTypeEntity> findByFilter(IdTypeEntity entity);

    List<IdTypeEntity> findAll(IdTypeEntity entity);

}

