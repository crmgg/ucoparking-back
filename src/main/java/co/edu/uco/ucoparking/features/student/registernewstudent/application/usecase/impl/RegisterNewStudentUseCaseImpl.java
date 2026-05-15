package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.StudentEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.StudentRepository;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.interactor.mapper.RegisterNewStudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterNewStudentUseCaseImpl implements RegisterNewStudentUseCase {

    private StudentRepository repository;
    private RegisterNewStudentMapper mapper;

    @Autowired
    public RegisterNewStudentUseCaseImpl(StudentRepository repository, RegisterNewStudentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Void execute(RegisterNewStudentDomain data) {
        // Ejecutar reglas de negocio aquí (ejemplo: validaciones adicionales, lógica de negocio)
        // ...
        // Usar MapStruct para convertir Domain a Entity
        StudentEntity entity = mapper.domainToEntity(data);
        repository.create(entity);
        return null;
    }
}
