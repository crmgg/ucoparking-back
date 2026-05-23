package co.edu.uco.ucoparking.infraestructure.persistence.repository;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface StudentRepository {

    Mono<Void> create(StudentEntity entity); // Debe implementarse de forma reactiva

    StudentEntity findById(UUID id);

    List<StudentEntity> findByFilter(StudentEntity entity);

    List<StudentEntity> findAll(StudentEntity entity); //Preguntar a farid si va con esto o solito ()

}
