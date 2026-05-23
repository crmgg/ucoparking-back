package co.edu.uco.ucoparking.application.usecase.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;

@Mapper(componentModel = "spring")
public interface RegisterNewStudentDomainDtoStudentEntityMapper extends MapperDomain<RegisterNewStudentDomain, RegisterNewStudentDTO> {

    @Override
    @Mapping(target = "id", ignore = true) // No hay setter para id en RegisterNewStudentDomain
    @Mapping(target = "academicProgram", source = "academicProgram")
    @Mapping(target = "idType", source = "idType")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "mobileNumber", source = "mobileNumber")
    RegisterNewStudentDTO toDTO(RegisterNewStudentDomain domain);

    @Override
    @Mapping(target = "id", ignore = true) // No hay setter para id en RegisterNewStudentDomain
    @Mapping(target = "academicProgram", source = "academicProgram")
    @Mapping(target = "idType", source = "idType")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "mobileNumber", source = "mobileNumber")
    RegisterNewStudentDomain toDomain(RegisterNewStudentDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "academicProgram", ignore = true)
    @Mapping(target = "idType", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "firstLastName", ignore = true)
    @Mapping(target = "secondLastName", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "mobileNumber")
    StudentEntity toEntity(RegisterNewStudentDomain domain);

}
