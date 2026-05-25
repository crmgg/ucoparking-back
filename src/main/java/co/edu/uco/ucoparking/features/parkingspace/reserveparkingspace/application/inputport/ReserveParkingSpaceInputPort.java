package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport;

import co.edu.uco.ucoparking.application.inputport.InputPort;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import reactor.core.publisher.Mono;

public interface ReserveParkingSpaceInputPort extends InputPort<ReserveParkingSpaceDTO, ParkingSpaceDTO> {
    @Override
    Mono<ParkingSpaceDTO> execute(ReserveParkingSpaceDTO data);
}
