package co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.StudentRepository;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.StudentJPARepository;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.StudentJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepositoryAdapter implements StudentRepository {

    private StudentJPARepository repository;

    public StudentRepositoryAdapter(StudentJPARepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public void create(StudentEntity entity) {
        StudentJpaEntity jpaEntity = null; //Mapper
        repository.save(jpaEntity);
    }

    @Override
    public StudentEntity findById(UUID id) {
        Optional<StudentJpaEntity> entity = repository.findById(id);
        StudentEntity entityReturn = null ;//Mapper

        return entityReturn;
    }

    @Override
    public List<StudentEntity> findByFilter(StudentEntity entity) {
        return List.of();
    }

    @Override
    public List<StudentEntity> findAll(StudentEntity entity) {
        return List.of();
    }
}
