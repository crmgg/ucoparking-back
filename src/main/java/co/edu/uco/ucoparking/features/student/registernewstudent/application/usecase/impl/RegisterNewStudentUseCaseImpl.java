package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl;

import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl.mapper.RegisterNewStudentDomainToStudentEntityMapper;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.StudentRepository;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RegisterNewStudentUseCaseImpl implements RegisterNewStudentUseCase {

    private StudentRepository repository;
    private RegisterNewStudentDomainToStudentEntityMapper mapper;

    @Autowired
    public RegisterNewStudentUseCaseImpl(
            StudentRepository repository,
            RegisterNewStudentDomainToStudentEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> execute(RegisterNewStudentDomain data) {
        StudentEntity entity = mapper.domainToEntity(data);
        return repository.create(entity).then();
    }
}