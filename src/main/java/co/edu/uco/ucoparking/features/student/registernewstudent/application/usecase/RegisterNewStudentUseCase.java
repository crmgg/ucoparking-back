package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase;

import co.edu.uco.ucoparking.application.usecase.UseCase;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import reactor.core.publisher.Mono;

public interface RegisterNewStudentUseCase extends UseCase<RegisterNewStudentDomain, Void> {
    @Override
    Mono<Void> execute(RegisterNewStudentDomain data);
}
