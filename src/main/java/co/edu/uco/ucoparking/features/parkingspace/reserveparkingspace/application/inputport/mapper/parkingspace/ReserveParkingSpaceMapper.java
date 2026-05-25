package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.mapper.parkingspace;

import co.edu.uco.ucoparking.application.inputport.mapper.DTOMapper;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReserveParkingSpaceMapper extends DTOMapper<ReserveParkingSpaceDTO, ReserveParkingSpaceDomain> {

    @Override
    @Mapping(target = "studentEmail", ignore = true)
    ReserveParkingSpaceDTO toDTO(ReserveParkingSpaceDomain domain);
}
