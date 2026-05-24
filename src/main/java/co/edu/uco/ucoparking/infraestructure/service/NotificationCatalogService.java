package co.edu.uco.ucoparking.infraestructure.service;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationTemplateResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class NotificationCatalogService {

    private final Environment environment;

    public NotificationCatalogService(Environment environment) {
        this.environment = environment;
    }

    public NotificationTemplateResponse getTemplate(String code) {
        String baseKey = "catalog.notifications." + code;
        String subject = environment.getProperty(baseKey + ".subject");
        String body = environment.getProperty(baseKey + ".body");
        if (subject == null || body == null) {
            throw UcoParkingException.create(
                    "Plantilla no encontrada: " + code,
                    "No existe plantilla en catalogo externo para key=" + baseKey);
        }

        var response = new NotificationTemplateResponse();
        response.setCode(code);
        response.setSubject(subject);
        response.setBody(body);
        return response;
    }
}
