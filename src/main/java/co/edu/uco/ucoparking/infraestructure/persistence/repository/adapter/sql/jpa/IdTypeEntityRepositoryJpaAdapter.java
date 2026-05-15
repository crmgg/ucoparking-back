package co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.IdTypeEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.IdTypeRepository;
import co.edu.uco.ucoparking.infraestructure.persistence.sql.IdTypeJPARepository;
import co.edu.uco.ucoparking.infraestructure.persistence.sql.entity.IdTypeJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IdTypeEntityRepositoryJpaAdapter implements IdTypeRepository {

    private IdTypeJPARepository repository;

    public IdTypeEntityRepositoryJpaAdapter(IdTypeJPARepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public void create(IdTypeEntity entity) {
        IdTypeJpaEntity jpaEntity = null; //Mapper
        repository.save(jpaEntity);
    }

    @Override
    public void update(IdTypeEntity entity) {
        IdTypeJpaEntity jpaEntity = null; //Mapper
        repository.save(jpaEntity);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public IdTypeEntity findById(UUID id) {
        Optional<IdTypeJpaEntity> optionalIdTypeJpaEntity = repository.findById(id);
        IdTypeEntity entityReturn = null ;//Mapper

        return entityReturn;
    }

    @Override
    public List<IdTypeEntity> findByFilter(IdTypeEntity entity) {
        return List.of();
    }

    @Override
    public List<IdTypeEntity> findAll(IdTypeEntity entity) {
        return List.of();
    }
}
