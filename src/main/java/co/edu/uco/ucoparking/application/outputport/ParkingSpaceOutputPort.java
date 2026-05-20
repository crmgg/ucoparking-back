package co.edu.uco.ucoparking.application.outputport;

import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParkingSpaceOutputPort {
    Flux<ParkingSpaceDTO> getAllParkingSpaces();
    Mono<ParkingSpaceDTO> getParkingSpaceByNumber(Integer spaceNumber);
    Flux<ParkingSpaceDTO> subscribeToParkingSpaceUpdates(Integer spaceNumber);
    Flux<ParkingSpaceDTO> getOccupiedParkingSpaces();
    Flux<ParkingSpaceDTO> getAvailableParkingSpaces();
}
