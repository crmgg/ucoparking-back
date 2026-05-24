package co.edu.uco.ucoparking.infraestructure.config;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class ParkingSpaceDataInitializer {

    private static final Logger log = LoggerFactory.getLogger(ParkingSpaceDataInitializer.class);
    private static final int TOTAL_SPACES = 11;

    private final ParkingSpaceRepository repository;

    public ParkingSpaceDataInitializer(ParkingSpaceRepository repository) {
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedParkingSpaces() {
        repository.count()
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.empty();
                    }
                    log.info("Inicializando {} parqueaderos", TOTAL_SPACES);
                    return Flux.range(1, TOTAL_SPACES)
                            .flatMap(spaceNumber -> {
                                var entity = new ParkingSpaceEntity();
                                entity.setId(UUID.randomUUID().toString());
                                entity.setSpaceNumber(spaceNumber);
                                entity.setStatus("AVAILABLE");
                                return repository.save(entity);
                            })
                            .then();
                })
                .block();
    }
}
