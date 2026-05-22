package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.ReserveParkingSpaceInputPort;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.mapper.parkingspace.ReserveParkingSpaceMapper;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.ReserveParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReserveParkingSpaceInteractor implements ReserveParkingSpaceInputPort {

    private ReserveParkingSpaceUseCase useCase;
    private ReserveParkingSpaceMapper mapper;

    public ReserveParkingSpaceInteractor(ReserveParkingSpaceUseCase useCase,
                                         ReserveParkingSpaceMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @Override
    public Void execute(ReserveParkingSpaceDTO data) {
        ReserveParkingSpaceDomain domain = mapper.toDomain(data);
        return useCase.execute(domain);
    }
}
