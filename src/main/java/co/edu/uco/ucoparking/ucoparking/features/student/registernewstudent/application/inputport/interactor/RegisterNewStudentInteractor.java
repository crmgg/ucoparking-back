package co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.inputport.interactor;

import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.inputport.RegisterNewStudentInputPort;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.inputport.to.RegisterNewStudentInputTO;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.inputport.mapper.RegisterNewStudentMapper;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegisterNewStudentInteractor implements RegisterNewStudentInputPort {

    private RegisterNewStudentUseCase useCase;
    private RegisterNewStudentMapper mapper;

    @Autowired
    public RegisterNewStudentInteractor(RegisterNewStudentUseCase useCase, RegisterNewStudentMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @Override
    public Void execute(RegisterNewStudentInputTO data) {

        // El usecase recibe un domain y el interactor recibe un DTO/TO
        // Usamos MapStruct para convertir TO a Domain
        RegisterNewStudentDomain domain = mapper.toDomain(data);
        return useCase.execute(domain);
    }
}
