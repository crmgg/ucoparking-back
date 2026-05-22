package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.mapper.parkingspace;

import co.edu.uco.ucoparking.application.inputport.mapper.DTOMapper;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReserveParkingSpaceMapper extends DTOMapper<ReserveParkingSpaceDTO, ReserveParkingSpaceDomain> {

}
