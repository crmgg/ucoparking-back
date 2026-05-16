package co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.RegisterNewStudentInputPort;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.mapper.student.RegisterNewStudentMapper;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegisterNewStudentInteractor implements RegisterNewStudentInputPort {

    private RegisterNewStudentUseCase useCase;
    private RegisterNewStudentMapper mapper;

    public RegisterNewStudentInteractor(RegisterNewStudentUseCase useCase,
                                        RegisterNewStudentMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @Override
    public Void execute(RegisterNewStudentDTO data) {          // ← Void con mayúscula
        RegisterNewStudentDomain domain = mapper.toDomain(data);
        return useCase.execute(domain);                        // ← return necesario
    }
}