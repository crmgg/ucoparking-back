package co.edu.uco.ucoparking.application.usecase;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.persistence.adapter.ParkingSpaceAdapter;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AdminReleaseParkingSpaceUseCase {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceAdapter parkingSpaceAdapter;

    @Value("${auth0.security.enabled:true}")
    private boolean securityEnabled;

    public AdminReleaseParkingSpaceUseCase(
            ParkingSpaceRepository parkingSpaceRepository,
            ParkingSpaceAdapter parkingSpaceAdapter) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceAdapter = parkingSpaceAdapter;
    }

    public Mono<ParkingSpaceDTO> execute(Integer spaceNumber) {
        if (securityEnabled) {
            return Mono.error(UcoParkingException.create(
                    "Liberacion admin no permitida con auth activa.",
                    "auth0.security.enabled=true"));
        }

        return parkingSpaceRepository.findBySpaceNumber(spaceNumber)
                .switchIfEmpty(Mono.error(UcoParkingException.create(
                        "El parqueadero " + spaceNumber + " no existe.",
                        "spaceNumber=" + spaceNumber)))
                .flatMap(this::clearAndSave)
                .map(this::mapEntityToDTO);
    }

    private Mono<ParkingSpaceEntity> clearAndSave(ParkingSpaceEntity space) {
        space.setStatus("AVAILABLE");
        space.setOccupiedByStudentId(null);
        space.setOccupiedByStudentName(null);
        space.setVehiclePlate(null);
        space.setReservationStartTime(null);
        space.setReservationEndTime(null);
        space.setReservationDate(null);
        space.setUpdatedAt(System.currentTimeMillis());

        return parkingSpaceRepository.save(space)
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
