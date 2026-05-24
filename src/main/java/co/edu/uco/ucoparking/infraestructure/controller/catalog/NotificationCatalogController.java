package co.edu.uco.ucoparking.infraestructure.controller.catalog;

import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationTemplateResponse;
import co.edu.uco.ucoparking.infraestructure.service.NotificationCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/notifications/templates")
public class NotificationCatalogController {

    private final NotificationCatalogService notificationCatalogService;

    public NotificationCatalogController(NotificationCatalogService notificationCatalogService) {
        this.notificationCatalogService = notificationCatalogService;
    }

    @GetMapping("/{code}")
    public Mono<NotificationTemplateResponse> getTemplate(@PathVariable String code) {
        return Mono.fromCallable(() -> notificationCatalogService.getTemplate(code));
    }
}
