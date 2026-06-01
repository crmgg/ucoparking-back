package co.edu.uco.ucoparking.infraestructure.scheduler;

import co.edu.uco.ucoparking.crosscutting.helper.ParkingSpaceReservationHelper;
import co.edu.uco.ucoparking.crosscutting.helper.ReservationDateHelper;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.persistence.adapter.ParkingSpaceAdapter;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ParkingSpaceDailyResetScheduler {

    private static final Logger log = LoggerFactory.getLogger(ParkingSpaceDailyResetScheduler.class);
    private static final String OCCUPIED_STATUS = "OCCUPIED";

    private final ParkingSpaceRepository repository;
    private final ParkingSpaceAdapter parkingSpaceAdapter;

    public ParkingSpaceDailyResetScheduler(
            ParkingSpaceRepository repository,
            ParkingSpaceAdapter parkingSpaceAdapter) {
        this.repository = repository;
        this.parkingSpaceAdapter = parkingSpaceAdapter;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void releaseExpiredOnStartup() {
        releaseExpiredReservations()
                .doOnSuccess(count -> {
                    if (count > 0) {
                        log.info("Liberados {} parqueaderos con reserva de dias anteriores al arranque", count);
                    }
                })
                .subscribe();
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void releaseExpiredAtMidnight() {
        releaseExpiredReservations()
                .doOnSuccess(count -> log.info("Medianoche Colombia: liberados {} parqueaderos vencidos", count))
                .subscribe();
    }

    Mono<Long> releaseExpiredReservations() {
        String today = ReservationDateHelper.today();
        return repository.findByStatusAndReservationDateNot(OCCUPIED_STATUS, today)
                .flatMap(this::clearAndSave)
                .count();
    }

    private Mono<ParkingSpaceEntity> clearAndSave(ParkingSpaceEntity space) {
        ParkingSpaceReservationHelper.clearReservation(space);
        return repository.save(space)
                .doOnNext(saved -> parkingSpaceAdapter.emitParkingSpaceUpdate(mapEntityToDTO(saved)));
    }

    private ParkingSpaceDTO mapEntityToDTO(ParkingSpaceEntity entity) {
        return new ParkingSpaceDTO(
                entity.getId(),
                entity.getSpaceNumber(),
                entity.getStatus(),
                entity.getOccupiedByStudentId(),
                entity.getOccupiedByStudentName(),
                entity.getVehiclePlate(),
                entity.getReservationStartTime(),
                entity.getReservationEndTime(),
                entity.getReservationDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
