package co.edu.uco.ucoparking.infraestructure.service;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.ParameterEntryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ParameterCatalogService {

    private static final Logger log = LoggerFactory.getLogger(ParameterCatalogService.class);

    private final Environment environment;

    public ParameterCatalogService(Environment environment) {
        this.environment = environment;
    }

    @Cacheable(value = "parameters", key = "#key")
    public ParameterEntryResponse getParameter(String key) {
        log.info("Cache miss para parametro: {}", key);
        String baseKey = "catalog.parameters." + key;
        String value = environment.getProperty(baseKey + ".value");
        if (value == null) {
            throw UcoParkingException.create(
                    "Parametro no encontrado: " + key,
                    "No existe parametro en catalogo externo para key=" + baseKey);
        }

        var response = new ParameterEntryResponse();
        response.setCode(key);
        response.setValue(value);
        response.setDescription(environment.getProperty(baseKey + ".description", ""));
        return response;
    }
}
