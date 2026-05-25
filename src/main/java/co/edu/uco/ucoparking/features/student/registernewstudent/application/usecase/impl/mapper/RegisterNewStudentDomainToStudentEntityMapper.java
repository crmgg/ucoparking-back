package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl.mapper;

import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.AcademicProgramJpaEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.IdTypeJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = {
        AcademicProgramJpaEntity.class,
        IdTypeJpaEntity.class
})
public interface RegisterNewStudentDomainToStudentEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "academicProgram", expression = "java(new AcademicProgramJpaEntity(domain.getAcademicProgram(), null, null))")
    @Mapping(target = "idType", expression = "java(new IdTypeJpaEntity(domain.getIdType()))")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "mobileNumber")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "firstLastName", ignore = true)
    @Mapping(target = "secondLastName", ignore = true)
    StudentEntity domainToEntity(RegisterNewStudentDomain domain);

    @AfterMapping
    default void mapNameParts(@MappingTarget StudentEntity entity, RegisterNewStudentDomain domain) {
        String[] parts = domain.getName().trim().split("\\s+");
        if (parts.length == 1) {
            entity.setName(parts[0]);
            entity.setFirstLastName("-");
            return;
        }
        if (parts.length == 2) {
            entity.setName(parts[0]);
            entity.setFirstLastName(parts[1]);
            return;
        }
        entity.setName(parts[0]);
        entity.setFirstLastName(parts[1]);
        entity.setSecondLastName(String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length)));
    }
}
