package co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.mapper.student;

import co.edu.uco.ucoparking.application.inputport.mapper.DTOMapper;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterNewStudentMapper extends DTOMapper<RegisterNewStudentDTO, RegisterNewStudentDomain> {

    @Override
    default RegisterNewStudentDomain toDomain(RegisterNewStudentDTO dto) {
        return new RegisterNewStudentDomain(
                dto.getAcademicProgram(),
                dto.getIdType(),
                dto.getIdNumber(),
                dto.getName(),
                dto.getEmail(),
                dto.getMobileNumber()
        );
    }

    @Override
    @Mapping(target = "id", source = "id")
    RegisterNewStudentDTO toDTO(RegisterNewStudentDomain domain);
}
