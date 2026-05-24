package co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.RegisterNewStudentInputPort;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.mapper.student.RegisterNewStudentMapper;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.validator.RegisterNewStudentValidator;
import reactor.core.publisher.Mono;

@Service
@Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
public class RegisterNewStudentInteractor implements RegisterNewStudentInputPort {

    private final RegisterNewStudentUseCase useCase;
    private final RegisterNewStudentMapper mapper;
    private final RegisterNewStudentValidator validator;

    public RegisterNewStudentInteractor(RegisterNewStudentUseCase useCase,
                                        RegisterNewStudentMapper mapper,
                                        RegisterNewStudentValidator validator) {
        this.useCase = useCase;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public Mono<Void> execute(RegisterNewStudentDTO data) {
        return Mono.fromRunnable(() -> validator.validate(data))
                .then(Mono.defer(() -> {
                    RegisterNewStudentDomain domain = mapper.toDomain(data);
                    return useCase.execute(domain);
                }));
    }
}
