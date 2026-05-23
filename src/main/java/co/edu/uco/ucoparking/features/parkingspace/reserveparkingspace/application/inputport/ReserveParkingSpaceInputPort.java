package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport;

import co.edu.uco.ucoparking.application.inputport.InputPort;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import reactor.core.publisher.Mono;

public interface ReserveParkingSpaceInputPort extends InputPort<ReserveParkingSpaceDTO, Void> {
    @Override
    Mono<Void> execute(ReserveParkingSpaceDTO data);
}
