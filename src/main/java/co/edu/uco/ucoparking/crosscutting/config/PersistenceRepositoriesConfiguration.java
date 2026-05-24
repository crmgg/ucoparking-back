package co.edu.uco.ucoparking.crosscutting.config;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import javax.sql.DataSource;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * Separa el escaneo de repositorios JPA y R2DBC. Además, en {@code application.properties}
 * se desactiva el auto-registro de repositorios R2DBC de Spring Boot para evitar que solo
 * escanee el paquete de arranque y no cree el bean de {@link ParkingSpaceRepository}.
 * <p>
 * Con {@code ConnectionFactory} (R2DBC) presente, Spring Boot no auto-configura el
 * {@link DataSource} JDBC; JPA necesita un {@code DataSource} explícito para el
 * {@code entityManagerFactory}.
 * <p>
 * Las entidades {@code @Entity} viven fuera del paquete de {@code @SpringBootApplication}
 * ({@code initializer}); hay que declarar explícitamente el escaneo JPA.
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@EntityScan(basePackages = "co.edu.uco.ucoparking.infraestructure.persistence")
@EnableJpaRepositories(basePackages = "co.edu.uco.ucoparking.infraestructure.persistence.repository.sql")
@EnableR2dbcRepositories(basePackageClasses = ParkingSpaceRepository.class)
public class PersistenceRepositoriesConfiguration {

    @Bean
    public DataSource jdbcDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
}
