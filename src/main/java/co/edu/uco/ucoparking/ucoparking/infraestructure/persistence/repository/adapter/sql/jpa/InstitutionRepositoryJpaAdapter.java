package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa;

import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.entity.InstitutionEntity;
import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.repository.InstitutionRepository;
import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.sql.InstitucionJPARepository;
import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.sql.entity.InstitutionJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InstitutionRepositoryJpaAdapter implements InstitutionRepository {

    private InstitucionJPARepository repository;

    public InstitutionRepositoryJpaAdapter(InstitucionJPARepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public void create(InstitutionEntity entity) {
        InstitutionJpaEntity jpaEntity = null; //Mapper
        repository.save(jpaEntity);
    }

    @Override
    public void update(InstitutionEntity entity) {
        InstitutionJpaEntity jpaEntity = null; //Mapper
        repository.save(jpaEntity);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public InstitutionEntity findById(UUID id) {
        Optional< InstitutionJpaEntity> jpaEntity = repository.findById(id);
        InstitutionEntity entityReturn = null ;//Mapper

        return entityReturn;
    }

    @Override
    public List<InstitutionEntity> findByFilter(InstitutionEntity entity) {
        return List.of();
    }

    @Override
    public List<InstitutionEntity> findAll(InstitutionEntity entity) {
        return List.of();
    }
}
