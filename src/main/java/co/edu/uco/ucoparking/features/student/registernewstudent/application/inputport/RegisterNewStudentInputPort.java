package co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport;

import co.edu.uco.ucoparking.application.inputport.InputPort;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import reactor.core.publisher.Mono;

public interface RegisterNewStudentInputPort extends InputPort<RegisterNewStudentDTO, Void> {
    @Override
    Mono<Void> execute(RegisterNewStudentDTO data);
}
