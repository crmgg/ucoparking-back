package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.impl.mapper;

import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain.ReserveParkingSpaceDomain;
import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReserveParkingSpaceDomainToParkingSpaceEntityMapper {

    @Mapping(source = "studentId", target = "occupiedByStudentId")
    ParkingSpaceEntity domainToEntity(ReserveParkingSpaceDomain domain);

}
