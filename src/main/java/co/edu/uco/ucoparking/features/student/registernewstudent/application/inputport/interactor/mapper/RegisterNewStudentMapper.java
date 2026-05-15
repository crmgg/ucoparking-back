package co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.interactor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.to.RegisterNewStudentInputTO;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.StudentEntity;

@Mapper(componentModel = "spring")
public interface RegisterNewStudentMapper {
    RegisterNewStudentMapper INSTANCE = Mappers.getMapper(RegisterNewStudentMapper.class);

    // DTO <-> Domain
    RegisterNewStudentDTO toDTO(RegisterNewStudentDomain domain);
    RegisterNewStudentDomain toDomain(RegisterNewStudentDTO dto);

    // TO <-> DTO
    RegisterNewStudentDTO toDTO(RegisterNewStudentInputTO to);
    RegisterNewStudentInputTO toTO(RegisterNewStudentDTO dto);

    // TO <-> Domain
    RegisterNewStudentDomain toDomain(RegisterNewStudentInputTO to);
    RegisterNewStudentInputTO toTO(RegisterNewStudentDomain domain);

    // Domain -> Entity (solo los campos básicos, relaciones deben resolverse aparte)
    @Mapping(target = "academicProgram", ignore = true)
    @Mapping(target = "idType", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "firstLastName", ignore = true)
    @Mapping(target = "secondLastName", ignore = true)
    @Mapping(target = "phoneNumber", source = "mobileNumber")
    StudentEntity domainToEntity(RegisterNewStudentDomain domain);
}
