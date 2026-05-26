package co.edu.uco.ucoparking.application.usecase;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.ParkingSpaceIsAvailableRule;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.StudentDoesNotHaveActiveParkingSpaceRule;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.persistence.adapter.ParkingSpaceAdapter;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OccupyParkingSpaceUseCase {

    private static final String OCCUPIED_STATUS = "OCCUPIED";

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceAdapter parkingSpaceAdapter;

    public OccupyParkingSpaceUseCase(ParkingSpaceRepository parkingSpaceRepository,
                                    ParkingSpaceAdapter parkingSpaceAdapter) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceAdapter = parkingSpaceAdapter;
    }

    public Mono<ParkingSpaceDTO> execute(Integer spaceNumber, String studentId, String studentName) {
        return parkingSpaceRepository.findBySpaceNumber(spaceNumber)
                .switchIfEmpty(Mono.error(UcoParkingException.create(
                        "El parqueadero " + spaceNumber + " no existe.",
                        "No se encontró parqueadero con número: " + spaceNumber)))
                .flatMap(space -> validateAndOccupy(space, studentId, studentName))
                .map(this::mapEntityToDTO);
    }

    private Mono<ParkingSpaceEntity> validateAndOccupy(ParkingSpaceEntity space, String studentId, String studentName) {
        ParkingSpaceIsAvailableRule.executeRule(space.getStatus(), space.getSpaceNumber());

        return parkingSpaceRepository.findByOccupiedByStudentIdAndStatus(studentId, OCCUPIED_STATUS)
                .hasElement()
                .flatMap(hasActiveSpace -> {
                    StudentDoesNotHaveActiveParkingSpaceRule.executeRule(hasActiveSpace);

                    space.setStatus(OCCUPIED_STATUS);
                    space.setOccupiedByStudentId(studentId);
                    space.setOccupiedByStudentName(studentName);
                    space.setUpdatedAt(System.currentTimeMillis());

                    return parkingSpaceRepository.save(space)
                            .doOnNext(updatedSpace -> {
                                ParkingSpaceDTO dto = mapEntityToDTO(updatedSpace);
                                parkingSpaceAdapter.emitParkingSpaceUpdate(dto);
                            });
                });
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
