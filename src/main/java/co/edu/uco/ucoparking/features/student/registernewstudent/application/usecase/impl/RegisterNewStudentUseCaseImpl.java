package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl;

import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl.mapper.RegisterNewStudentDomainToStudentEntityMapper;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.StudentRepository;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterNewStudentUseCaseImpl implements RegisterNewStudentUseCase {

    private StudentRepository repository;
    private RegisterNewStudentDomainToStudentEntityMapper mapper; // ← tipo corregido

    @Autowired
    public RegisterNewStudentUseCaseImpl(
            StudentRepository repository,
            RegisterNewStudentDomainToStudentEntityMapper mapper) { // ← tipo corregido
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Void execute(RegisterNewStudentDomain data) {
        StudentEntity entity = mapper.domainToEntity(data);
        repository.create(entity);
        return null;
    }
}