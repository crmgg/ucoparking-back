package co.edu.uco.ucoparking.infraestructure.config;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@DependsOn("entityManagerFactory")
public class ParkingSpaceDataInitializer {

    private static final Logger log = LoggerFactory.getLogger(ParkingSpaceDataInitializer.class);
    private static final int TOTAL_SPACES = 11;

    private final ParkingSpaceRepository repository;

    public ParkingSpaceDataInitializer(ParkingSpaceRepository repository) {
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedParkingSpaces() {
        log.info("[SEED] Initializing parking spaces...");
        try {
            Long existing = repository.count().block();
            log.info("[SEED] Existing spaces: {}", existing);

            if (existing != null && existing > 0) {
                log.info("[SEED] Seed skipped, table already has data");
                return;
            }

            log.info("[SEED] Creating {} parking spaces...", TOTAL_SPACES);

            Long inserted = Flux.range(1, TOTAL_SPACES)
                    .concatMap(spaceNumber -> {
                        var entity = new ParkingSpaceEntity();
                        entity.setSpaceNumber(spaceNumber);
                        entity.setStatus("AVAILABLE");
                        return repository.save(entity)
                                .doOnSuccess(saved -> log.info("[SEED] Created space {}", spaceNumber));
                    })
                    .count()
                    .block();

            Long total = repository.count().block();
            log.info("[SEED] Seed completed successfully. Inserted: {}, total in DB: {}", inserted, total);

            if (total == null || total < TOTAL_SPACES) {
                throw new IllegalStateException(
                        "[SEED] Expected at least " + TOTAL_SPACES + " parking spaces but found " + total);
            }
        } catch (Exception exception) {
            log.error("[SEED] Seed failed (la app sigue arriba; revisa SQL en localhost:14333)", exception);
        }
    }
}
