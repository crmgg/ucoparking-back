package co.edu.uco.ucoparking.infraestructure.persistence.adapter;

import co.edu.uco.ucoparking.application.outputport.ParkingSpaceOutputPort;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class ParkingSpaceAdapter implements ParkingSpaceOutputPort {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final Sinks.Many<ParkingSpaceDTO> parkingSpaceUpdates;

    public ParkingSpaceAdapter(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceUpdates = Sinks.many().multicast().onBackpressureBuffer(1000);
    }

    @Override
    public Flux<ParkingSpaceDTO> getAllParkingSpaces() {
        return parkingSpaceRepository.findAllByOrderBySpaceNumber()
                .map(this::mapEntityToDTO);
    }

    @Override
    public Mono<ParkingSpaceDTO> getParkingSpaceByNumber(Integer spaceNumber) {
        return parkingSpaceRepository.findBySpaceNumber(spaceNumber)
                .map(this::mapEntityToDTO);
    }

    @Override
    public Flux<ParkingSpaceDTO> subscribeToParkingSpaceUpdates(Integer spaceNumber) {
        return Flux.merge(
                getParkingSpaceByNumber(spaceNumber).flux(),
                parkingSpaceUpdates.asFlux()
                        .filter(dto -> dto.getSpaceNumber().equals(spaceNumber))
        );
    }

    @Override
    public Flux<ParkingSpaceDTO> getOccupiedParkingSpaces() {
        return parkingSpaceRepository.findByStatus("OCCUPIED")
                .map(this::mapEntityToDTO);
    }

    @Override
    public Flux<ParkingSpaceDTO> getAvailableParkingSpaces() {
        return parkingSpaceRepository.findByStatus("AVAILABLE")
                .map(this::mapEntityToDTO);
    }

    public void emitParkingSpaceUpdate(ParkingSpaceDTO updatedSpace) {
        this.parkingSpaceUpdates.tryEmitNext(updatedSpace);
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
