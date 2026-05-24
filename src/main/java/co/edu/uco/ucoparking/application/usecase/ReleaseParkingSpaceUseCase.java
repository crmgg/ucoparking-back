package co.edu.uco.ucoparking.application.usecase;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.persistence.adapter.ParkingSpaceAdapter;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReleaseParkingSpaceUseCase {

    private static final String AVAILABLE_STATUS = "AVAILABLE";
    private static final String OCCUPIED_STATUS = "OCCUPIED";

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceAdapter parkingSpaceAdapter;

    public ReleaseParkingSpaceUseCase(ParkingSpaceRepository parkingSpaceRepository,
                                      ParkingSpaceAdapter parkingSpaceAdapter) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceAdapter = parkingSpaceAdapter;
    }

    public Mono<ParkingSpaceDTO> execute(Integer spaceNumber, String studentId) {
        return parkingSpaceRepository.findBySpaceNumber(spaceNumber)
                .switchIfEmpty(Mono.error(UcoParkingException.create(
                        "El parqueadero " + spaceNumber + " no existe.",
                        "No se encontró parqueadero con número: " + spaceNumber)))
                .flatMap(space -> validateAndRelease(space, studentId))
                .map(this::mapEntityToDTO);
    }

    private Mono<ParkingSpaceEntity> validateAndRelease(ParkingSpaceEntity space, String studentId) {
        if (!OCCUPIED_STATUS.equals(space.getStatus())) {
            throw UcoParkingException.create(
                    "El parqueadero " + space.getSpaceNumber() + " no tiene una reserva activa.",
                    "Estado actual: " + space.getStatus());
        }

        String ownerId = space.getOccupiedByStudentId();
        if (ownerId == null || !ownerId.equalsIgnoreCase(studentId)) {
            throw UcoParkingException.create(
                    "No puedes cancelar una reserva que no te pertenece.",
                    "occupiedByStudentId=" + ownerId + " studentId=" + studentId);
        }

        space.setStatus(AVAILABLE_STATUS);
        space.setOccupiedByStudentId(null);
        space.setOccupiedByStudentName(null);
        space.setUpdatedAt(System.currentTimeMillis());

        return parkingSpaceRepository.save(space)
                .doOnNext(updatedSpace ->
                        parkingSpaceAdapter.emitParkingSpaceUpdate(mapEntityToDTO(updatedSpace)));
    }

    private ParkingSpaceDTO mapEntityToDTO(ParkingSpaceEntity entity) {
        return new ParkingSpaceDTO(
                entity.getId(),
                entity.getSpaceNumber(),
                entity.getStatus(),
                entity.getOccupiedByStudentId(),
                entity.getOccupiedByStudentName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
