package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase;

import co.edu.uco.ucoparking.application.usecase.UseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import reactor.core.publisher.Mono;

public interface ReserveParkingSpaceUseCase extends UseCase<ReserveParkingSpaceDomain, ParkingSpaceDTO> {
    @Override
    Mono<ParkingSpaceDTO> execute(ReserveParkingSpaceDomain data);
}
