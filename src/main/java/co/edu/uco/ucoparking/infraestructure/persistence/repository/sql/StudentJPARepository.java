package co.edu.uco.ucoparking.infraestructure.persistence.repository.sql;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.StudentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentJPARepository extends JpaRepository<StudentJpaEntity, UUID> {
}
