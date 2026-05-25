package co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.mapper;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.AcademicProgramJpaEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.IdTypeJpaEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.StudentJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentJpaMapper {

    StudentEntity toEntity(StudentJpaEntity jpaEntity);

    default StudentJpaEntity toJpaEntity(
            StudentEntity entity,
            AcademicProgramJpaEntity academicProgram,
            IdTypeJpaEntity idType) {
        return new StudentJpaEntity(
                entity.getId(),
                academicProgram,
                idType,
                entity.getName(),
                entity.getFirstLastName(),
                entity.getSecondLastName(),
                entity.getEmail(),
                entity.getPhoneNumber()
        );
    }
}
