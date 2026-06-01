package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.impl;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.crosscutting.helper.ParkingSpaceReservationHelper;
import co.edu.uco.ucoparking.crosscutting.helper.ReservationDateHelper;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.ReserveParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.ParkingSpaceIsAvailableRule;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.StudentDoesNotHaveActiveParkingSpaceRule;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.VehiclePlateNotAlreadyReservedTodayRule;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.persistence.adapter.ParkingSpaceAdapter;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.r2dbc.ParkingSpaceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReserveParkingSpaceUseCaseImpl implements ReserveParkingSpaceUseCase {

    private static final String OCCUPIED_STATUS = "OCCUPIED";

    private final ParkingSpaceRepository repository;
    private final ParkingSpaceAdapter parkingSpaceAdapter;

    public ReserveParkingSpaceUseCaseImpl(
            ParkingSpaceRepository repository,
            ParkingSpaceAdapter parkingSpaceAdapter) {
        this.repository = repository;
        this.parkingSpaceAdapter = parkingSpaceAdapter;
    }

    @Override
    public Mono<ParkingSpaceDTO> execute(ReserveParkingSpaceDomain data) {
        String reservationDate = ReservationDateHelper.today();
        String normalizedPlate = ReservationDateHelper.normalizePlate(data.getVehiclePlate());

        return repository.findBySpaceNumber(data.getSpaceNumber())
                .switchIfEmpty(Mono.error(UcoParkingException.create(
                        "El parqueadero " + data.getSpaceNumber() + " no existe.",
                        "No se encontró parqueadero con número: " + data.getSpaceNumber())))
                .flatMap(space -> releaseIfNotToday(space)
                        .flatMap(freshSpace -> validatePlateAvailableToday(normalizedPlate, reservationDate)
                                .then(validateAndReserve(freshSpace, data, normalizedPlate, reservationDate))))
                .map(this::mapEntityToDTO);
    }

    private Mono<ParkingSpaceEntity> releaseIfNotToday(ParkingSpaceEntity space) {
        if (ParkingSpaceReservationHelper.isActiveToday(space)) {
            return Mono.just(space);
        }
        if (!OCCUPIED_STATUS.equals(space.getStatus())) {
            return Mono.just(space);
        }

        ParkingSpaceReservationHelper.clearReservation(space);
        return repository.save(space)
                .doOnNext(saved -> parkingSpaceAdapter.emitParkingSpaceUpdate(mapEntityToDTO(saved)));
    }

    private Mono<Void> validatePlateAvailableToday(String normalizedPlate, String reservationDate) {
        if (normalizedPlate == null || normalizedPlate.isBlank()) {
            return Mono.empty();
        }

        return repository
                .findByVehiclePlateIgnoreCaseAndStatusAndReservationDate(
                        normalizedPlate, OCCUPIED_STATUS, reservationDate)
                .hasElement()
                .flatMap(alreadyReserved -> {
                    VehiclePlateNotAlreadyReservedTodayRule.executeRule(alreadyReserved);
                    return Mono.empty();
                });
    }

    private Mono<ParkingSpaceEntity> validateAndReserve(
            ParkingSpaceEntity space,
            ReserveParkingSpaceDomain data,
            String normalizedPlate,
            String reservationDate) {
        ParkingSpaceIsAvailableRule.executeRule(space.getStatus(), space.getSpaceNumber());

        return repository.findByOccupiedByStudentIdAndStatusAndReservationDate(
                        data.getStudentId(), OCCUPIED_STATUS, reservationDate)
                .hasElement()
                .flatMap(hasActiveSpace -> {
                    StudentDoesNotHaveActiveParkingSpaceRule.executeRule(hasActiveSpace);

                    space.setStatus(OCCUPIED_STATUS);
                    space.setOccupiedByStudentId(data.getStudentId());
                    space.setOccupiedByStudentName(data.getStudentName());
                    space.setVehiclePlate(normalizedPlate);
                    space.setReservationStartTime(data.getReservationStartTime());
                    space.setReservationEndTime(data.getReservationEndTime());
                    space.setReservationDate(reservationDate);
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
                entity.getVehiclePlate(),
                entity.getReservationStartTime(),
                entity.getReservationEndTime(),
                entity.getReservationDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
