package co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.AcademicProgramEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.AcademicProgramRepository;
import co.edu.uco.ucoparking.infraestructure.persistence.sql.AcademicProgramJPARepository;
import co.edu.uco.ucoparking.infraestructure.persistence.sql.entity.AcademicProgramJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class AcademicProgramRepositoryJpaAdapter implements AcademicProgramRepository {

   private AcademicProgramJPARepository repository;

    public AcademicProgramRepositoryJpaAdapter(AcademicProgramJPARepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public void create(AcademicProgramEntity entity) {
        AcademicProgramJpaEntity jpaEntity = null; //Mapper
        repository.save(jpaEntity);

    }

    @Override
    public void update(AcademicProgramEntity entity) {
        AcademicProgramJpaEntity jpaEntity = null; //Mapper
        repository.save(jpaEntity);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public AcademicProgramEntity findById(UUID id) {
        Optional<AcademicProgramJpaEntity> entity = repository.findById(id);
        AcademicProgramEntity entityReturn = null ;//Mapper

        return entityReturn;
    }

    @Override
    public List<AcademicProgramEntity> findByFilter(AcademicProgramEntity entity) {
        return List.of();
    }

    @Override
    public List<AcademicProgramEntity> findAll(AcademicProgramEntity entity) {
        return List.of();
    }
}
