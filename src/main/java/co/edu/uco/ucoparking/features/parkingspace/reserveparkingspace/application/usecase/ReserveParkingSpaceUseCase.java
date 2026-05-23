package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase;

import co.edu.uco.ucoparking.application.usecase.UseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import reactor.core.publisher.Mono;

public interface ReserveParkingSpaceUseCase extends UseCase<ReserveParkingSpaceDomain, Void> {
    @Override
    Mono<Void> execute(ReserveParkingSpaceDomain data);
}
