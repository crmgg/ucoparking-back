package co.edu.uco.ucoparking.infraestructure.persistence.repository.sql;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.StudentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentJPARepository extends JpaRepository<StudentJpaEntity, UUID> {

    Optional<StudentJpaEntity> findFirstByEmailIgnoreCase(String email);

    Optional<StudentJpaEntity> findFirstByNameIgnoreCase(String name);

    Optional<StudentJpaEntity> findFirstByNameIgnoreCaseAndFirstLastNameIgnoreCase(
            String name, String firstLastName);

    @Query("SELECT s FROM StudentJpaEntity s WHERE LOWER(CONCAT(s.name, ' ', s.firstLastName)) = LOWER(:fullName)")
    Optional<StudentJpaEntity> findFirstByFullNameIgnoreCase(@Param("fullName") String fullName);
}
