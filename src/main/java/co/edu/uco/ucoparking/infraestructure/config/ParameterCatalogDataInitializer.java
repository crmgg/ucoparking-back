package co.edu.uco.ucoparking.infraestructure.config;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParameterCatalogEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.ParameterCatalogJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ParameterCatalogDataInitializer {

    private static final Logger log = LoggerFactory.getLogger(ParameterCatalogDataInitializer.class);

    private final ParameterCatalogJpaRepository repository;

    public ParameterCatalogDataInitializer(ParameterCatalogJpaRepository repository) {
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedParameters() {
        if (repository.count() > 0) {
            return;
        }
        log.info("Inicializando catalogo de parametros");
        save("PARKING_MAX_SPACES", "11", "Numero maximo de parqueaderos disponibles");
        save("PARKING_RESERVATION_TTL_MINUTES", "480", "Minutos de validez de una reserva activa");
    }

    private void save(String key, String value, String description) {
        var entity = new ParameterCatalogEntity();
        entity.setParamKey(key);
        entity.setParamValue(value);
        entity.setDescription(description);
        repository.save(entity);
    }
}
