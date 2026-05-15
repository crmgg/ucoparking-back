package co.edu.uco.ucoparking.infraestructure.persistence.sql;

import co.edu.uco.ucoparking.infraestructure.persistence.sql.entity.AcademicProgramJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AcademicProgramJPARepository extends JpaRepository<AcademicProgramJpaEntity, UUID> {
}
