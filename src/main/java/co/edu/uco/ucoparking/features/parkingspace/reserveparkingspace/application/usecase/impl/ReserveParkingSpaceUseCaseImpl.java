package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.impl;

import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.ReserveParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.impl.mapper.ReserveParkingSpaceDomainToParkingSpaceEntityMapper;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.ParkingSpaceRepository;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Void execute(ReserveParkingSpaceDomain data) {
        ParkingSpaceEntity entity = mapper.domainToEntity(data);
        repository.create(entity);
        return null;
    }
}
