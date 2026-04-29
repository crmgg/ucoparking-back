package co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.inputport.interactor;

import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.inputport.RegisterNewStudentInputPort;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.inputport.to.RegisterNewStudentInputTO;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegisterNewStudentInteractor implements RegisterNewStudentInputPort {

    private RegisterNewStudentUseCase useCase;

    public  RegisterNewStudentInteractor(RegisterNewStudentUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Void execute(RegisterNewStudentInputTO data) {

        //Recordar que el usecase recibe un domaint y el interractor recibe un DTO
        //Model Mapper o MapStruct
        RegisterNewStudentDomain domain = null;

        //Como solucionar este problema
        return useCase.execute(null);
    }

}
