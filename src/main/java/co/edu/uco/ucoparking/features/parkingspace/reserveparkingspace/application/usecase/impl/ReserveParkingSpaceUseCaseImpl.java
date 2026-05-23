package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.impl;

import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.ReserveParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.impl.mapper.ReserveParkingSpaceDomainToParkingSpaceEntityMapper;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReserveParkingSpaceUseCaseImpl implements ReserveParkingSpaceUseCase {

    private ParkingSpaceRepository repository;
    private ReserveParkingSpaceDomainToParkingSpaceEntityMapper mapper;

    @Autowired
    public ReserveParkingSpaceUseCaseImpl(
            ParkingSpaceRepository repository,
            ReserveParkingSpaceDomainToParkingSpaceEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> execute(ReserveParkingSpaceDomain data) {
        ParkingSpaceEntity entity = mapper.domainToEntity(data);
        return repository.save(entity).then();
    }
}
