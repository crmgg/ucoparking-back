package co.edu.uco.ucoparking.infraestructure.persistence.repository;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.InstitutionEntity;

import java.util.List;
import java.util.UUID;

public interface InstitutionRepository {

    void create(InstitutionEntity entity);

    void update(InstitutionEntity entity);

    void delete(UUID id);

    InstitutionEntity findById(UUID id);

    List<InstitutionEntity> findByFilter(InstitutionEntity entity);

    List<InstitutionEntity> findAll(InstitutionEntity entity);

}
