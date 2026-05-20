package co.edu.uco.ucoparking.infraestructure.persistence.repository;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ParkingSpaceRepository extends ReactiveCrudRepository<ParkingSpaceEntity, String> {
    Mono<ParkingSpaceEntity> findBySpaceNumber(Integer spaceNumber);
    Flux<ParkingSpaceEntity> findByStatus(String status);
    Flux<ParkingSpaceEntity> findAllByOrderBySpaceNumber();
}
