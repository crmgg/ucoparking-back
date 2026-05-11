package co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.impl;

import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.entity.StudentEntity;
import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.repository.StudentRepository;
import org.springframework.stereotype.Service;

import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;

@Service
public class RegisterNewStudentUseCaseImpl implements RegisterNewStudentUseCase {

    private StudentRepository repository;

    @Override
    public Void execute(RegisterNewStudentDomain data) {

        //Ejecutar reglas de negocio


        StudentEntity entity = null; //Mapper domai a Entity
        repository.create(entity);

        return null;
    }
}
