package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.validator;

import co.edu.uco.ucoparking.application.usecase.rule.generics.IsStringPresentRule;
import co.edu.uco.ucoparking.application.usecase.rule.generics.IsUUIDPresentRule;
import co.edu.uco.ucoparking.application.usecase.rule.generics.StringLengthValueIsValidRule;
import co.edu.uco.ucoparking.application.usecase.validator.Validator;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.rule.StudentEmailFormatValueIsValidRule;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.rule.StudentMobileNumberFormatValueIsValidRule;
import org.springframework.stereotype.Component;

@Component
public class RegisterNewStudentValidator implements Validator {

    @Override
    public void validate(Object... data) {
        var dto = (RegisterNewStudentDTO) data[0];

        IsUUIDPresentRule.executeRule(dto.getAcademicProgram(), "programa académico");
        IsUUIDPresentRule.executeRule(dto.getIdType(), "tipo de identificación");
        IsStringPresentRule.executeRule(dto.getIdNumber(), "número de identificación", true);
        StringLengthValueIsValidRule.executeRule(dto.getIdNumber(), "número de identificación", 5, 20, true);
        IsStringPresentRule.executeRule(dto.getName(), "nombre", true);
        StringLengthValueIsValidRule.executeRule(dto.getName(), "nombre", 2, 100, true);
        IsStringPresentRule.executeRule(dto.getEmail(), "correo electrónico", true);
        StudentEmailFormatValueIsValidRule.executeRule(dto.getEmail());
        IsStringPresentRule.executeRule(dto.getMobileNumber(), "número de celular", true);
        StudentMobileNumberFormatValueIsValidRule.executeRule(dto.getMobileNumber());
    }
}
