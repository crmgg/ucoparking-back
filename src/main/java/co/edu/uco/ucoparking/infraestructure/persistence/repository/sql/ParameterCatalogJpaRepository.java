package co.edu.uco.ucoparking.infraestructure.persistence.repository.sql;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParameterCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterCatalogJpaRepository extends JpaRepository<ParameterCatalogEntity, String> {
}
