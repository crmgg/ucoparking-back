package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.impl;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.ReserveParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.ParkingSpaceIsAvailableRule;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.StudentDoesNotHaveActiveParkingSpaceRule;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.persistence.adapter.ParkingSpaceAdapter;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReserveParkingSpaceUseCaseImpl implements ReserveParkingSpaceUseCase {

    private static final String OCCUPIED_STATUS = "OCCUPIED";

    private final ParkingSpaceRepository repository;
    private final ParkingSpaceAdapter parkingSpaceAdapter;

    @Autowired
    public ReserveParkingSpaceUseCaseImpl(
            ParkingSpaceRepository repository,
            ParkingSpaceAdapter parkingSpaceAdapter) {
        this.repository = repository;
        this.parkingSpaceAdapter = parkingSpaceAdapter;
    }

    @Override
    public Mono<Void> execute(ReserveParkingSpaceDomain data) {
        return repository.findBySpaceNumber(data.getSpaceNumber())
                .switchIfEmpty(Mono.error(UcoParkingException.create(
                        "El parqueadero " + data.getSpaceNumber() + " no existe.",
                        "No se encontró parqueadero con número: " + data.getSpaceNumber())))
                .flatMap(space -> validateAndReserve(space, data))
                .then();
    }

    private Mono<ParkingSpaceEntity> validateAndReserve(ParkingSpaceEntity space, ReserveParkingSpaceDomain data) {
        ParkingSpaceIsAvailableRule.executeRule(space.getStatus(), space.getSpaceNumber());

        return repository.findByOccupiedByStudentIdAndStatus(data.getStudentId().toString(), OCCUPIED_STATUS)
                .hasElement()
                .flatMap(hasActiveSpace -> {
                    StudentDoesNotHaveActiveParkingSpaceRule.executeRule(hasActiveSpace);

                    space.setStatus(OCCUPIED_STATUS);
                    space.setOccupiedByStudentId(data.getStudentId().toString());
                    space.setOccupiedByStudentName(data.getStudentName());
                    space.setUpdatedAt(System.currentTimeMillis());

                    return repository.save(space)
                            .doOnNext(savedSpace ->
                                    parkingSpaceAdapter.emitParkingSpaceUpdate(mapEntityToDTO(savedSpace)));
                });
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
