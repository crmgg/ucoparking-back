package co.edu.uco.ucoparking.application.usecase.mapper;

public interface MapperDomain <D, T> {

    /**
     * Convierte un DTO a un objeto de dominio
     *
     * @param dto el DTO a convertir
     * @return el objeto de dominio convertido
     */
    D toDomain(T dto);

    /**
     * Convierte un objeto de dominio a un DTO
     *
     * @param domain el objeto de dominio a convertir
     * @return el DTO convertido
     */
    T toDTO(D domain);

}
