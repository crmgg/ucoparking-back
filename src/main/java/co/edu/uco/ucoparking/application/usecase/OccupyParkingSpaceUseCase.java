package co.edu.uco.ucoparking.application.usecase;

import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.persistence.adapter.ParkingSpaceAdapter;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OccupyParkingSpaceUseCase {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceAdapter parkingSpaceAdapter;

    public OccupyParkingSpaceUseCase(ParkingSpaceRepository parkingSpaceRepository,
                                    ParkingSpaceAdapter parkingSpaceAdapter) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceAdapter = parkingSpaceAdapter;
    }

    public Mono<ParkingSpaceDTO> execute(Integer spaceNumber, String studentId, String studentName) {
        return parkingSpaceRepository.findBySpaceNumber(spaceNumber)
                .flatMap(space -> {
                    if (!"AVAILABLE".equals(space.getStatus())) {
                        return Mono.error(new IllegalStateException(
                                "El espacio " + spaceNumber + " no está disponible"));
                    }

                    space.setStatus("OCCUPIED");
                    space.setOccupiedByStudentId(studentId);
                    space.setOccupiedByStudentName(studentName);
                    space.setUpdatedAt(System.currentTimeMillis());

                    return parkingSpaceRepository.save(space)
                            .doOnNext(updatedSpace -> {
                                ParkingSpaceDTO dto = mapEntityToDTO(updatedSpace);
                                parkingSpaceAdapter.emitParkingSpaceUpdate(dto);
                            });
                })
                .map(this::mapEntityToDTO);
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
