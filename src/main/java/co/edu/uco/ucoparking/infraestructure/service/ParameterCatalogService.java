package co.edu.uco.ucoparking.infraestructure.service;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.ParameterEntryResponse;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.ParameterCatalogJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ParameterCatalogService {

    private static final Logger log = LoggerFactory.getLogger(ParameterCatalogService.class);

    private final ParameterCatalogJpaRepository repository;

    public ParameterCatalogService(ParameterCatalogJpaRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "parameters", key = "#key")
    public ParameterEntryResponse getParameter(String key) {
        log.info("Cache miss para parametro: {}", key);
        return repository.findById(key)
                .map(entity -> {
                    var response = new ParameterEntryResponse();
                    response.setCode(entity.getParamKey());
                    response.setValue(entity.getParamValue());
                    response.setDescription(entity.getDescription());
                    return response;
                })
                .orElseThrow(() -> UcoParkingException.create(
                        "Parametro no encontrado: " + key,
                        "No existe parametro en catalogo para key=" + key));
    }
}
