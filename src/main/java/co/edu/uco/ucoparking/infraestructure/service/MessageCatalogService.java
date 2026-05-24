package co.edu.uco.ucoparking.infraestructure.service;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class MessageCatalogService {

    private static final String DEFAULT_LOCALE = "es";

    private final Environment environment;

    public MessageCatalogService(Environment environment) {
        this.environment = environment;
    }

    public String getMessage(String code, String locale) {
        String resolvedLocale = locale != null && !locale.isBlank() ? locale : DEFAULT_LOCALE;
        String key = "catalog.messages." + code + "." + resolvedLocale;
        String message = environment.getProperty(key);
        if (message == null) {
            throw UcoParkingException.create(
                    "Mensaje no encontrado: " + code,
                    "No existe mensaje en catalogo externo para key=" + key);
        }
        return message;
    }
}
