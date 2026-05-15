package co.edu.uco.ucoparking.application.inputport.mapper;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DTOMapper <T, D> {

    T toDTO(D domain);

    D toDomain(T dto);

}
