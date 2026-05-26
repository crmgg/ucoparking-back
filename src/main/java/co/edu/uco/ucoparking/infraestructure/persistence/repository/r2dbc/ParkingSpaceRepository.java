package co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ParkingSpaceRepository extends ReactiveCrudRepository<ParkingSpaceEntity, String> {
    Mono<ParkingSpaceEntity> findBySpaceNumber(Integer spaceNumber);
    Mono<ParkingSpaceEntity> findByOccupiedByStudentIdAndStatus(String occupiedByStudentId, String status);
    Mono<ParkingSpaceEntity> findByVehiclePlateIgnoreCaseAndStatusAndReservationDate(
            String vehiclePlate, String status, String reservationDate);
    Flux<ParkingSpaceEntity> findByStatus(String status);
    Flux<ParkingSpaceEntity> findAllByOrderBySpaceNumber();
}
