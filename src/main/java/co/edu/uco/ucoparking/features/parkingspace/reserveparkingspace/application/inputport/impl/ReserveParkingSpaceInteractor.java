package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.impl;

import org.springframework.stereotype.Service;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.ReserveParkingSpaceInputPort;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.mapper.parkingspace.ReserveParkingSpaceMapper;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.ReserveParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.validator.ReserveParkingSpaceValidator;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import reactor.core.publisher.Mono;

@Service
public class ReserveParkingSpaceInteractor implements ReserveParkingSpaceInputPort {

    private final ReserveParkingSpaceUseCase useCase;
    private final ReserveParkingSpaceMapper mapper;
    private final ReserveParkingSpaceValidator validator;

    public ReserveParkingSpaceInteractor(ReserveParkingSpaceUseCase useCase,
                                         ReserveParkingSpaceMapper mapper,
                                         ReserveParkingSpaceValidator validator) {
        this.useCase = useCase;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public Mono<ParkingSpaceDTO> execute(ReserveParkingSpaceDTO data) {
        if (data.getStatus() == null || data.getStatus().isBlank()) {
            data.setStatus("OCCUPIED");
        }

        return Mono.fromRunnable(() -> validator.validate(data))
                .then(Mono.defer(() -> {
                    ReserveParkingSpaceDomain domain = mapper.toDomain(data);
                    return useCase.execute(domain);
                }));
    }
}
