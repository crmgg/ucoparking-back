package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl.mapper;

import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.AcademicProgramJpaEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.IdTypeJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {
        AcademicProgramJpaEntity.class,
        IdTypeJpaEntity.class
})
public interface RegisterNewStudentDomainToStudentEntityMapper {

    @Mapping(target = "academicProgram", expression = "java(new AcademicProgramJpaEntity(domain.getAcademicProgram(), null, null))")
    @Mapping(target = "idType", expression = "java(new IdTypeJpaEntity(domain.getIdType()))")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "firstLastName", ignore = true)
    @Mapping(target = "secondLastName", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)

    // Se ignoran campos que no existen en el dominio
    StudentEntity domainToEntity(RegisterNewStudentDomain domain);


}