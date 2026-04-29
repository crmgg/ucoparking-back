package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.repository;

import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.entity.StudentEntity;

import java.util.List;
import java.util.UUID;

public interface StudentRepository {

    void create(StudentEntity entity);

    void update(StudentEntity entity);

    void delete(StudentEntity entity);

    StudentEntity findById(UUID id);

    List<StudentEntity> findByFilter(StudentEntity entity);

    List<StudentEntity> findAll(StudentEntity entity);

}

