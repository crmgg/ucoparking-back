package co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.RegisterNewStudentUseCase;
import co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;

@Service
public class RegisterNewStudentUseCaseImpl implements RegisterNewStudentUseCase {

    @Override
    public Void execute(RegisterNewStudentDomain data) {
        return null;
    }
}
