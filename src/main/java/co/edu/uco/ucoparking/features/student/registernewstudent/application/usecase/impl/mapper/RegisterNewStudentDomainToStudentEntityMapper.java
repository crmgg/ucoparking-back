package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl.mapper;

import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterNewStudentDomainToStudentEntityMapper {

    @Mapping(source = "academicProgram", target = "academicProgramEntity")
    @Mapping(source = "idType", target = "idTypeEntity")
    StudentEntity domainToEntity(RegisterNewStudentDomain domain);

}