package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.sql;


import co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.sql.entity.InstitutionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstitucionJPARepository extends JpaRepository<InstitutionJpaEntity, UUID> {
}
