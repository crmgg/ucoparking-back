package co.edu.uco.ucoparking.infraestructure.controller.catalog;

import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationSendRequest;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationSendResponse;
import co.edu.uco.ucoparking.infraestructure.service.NotificationCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/notifications")
public class NotificationGatewayController {

    private static final Logger log = LoggerFactory.getLogger(NotificationGatewayController.class);

    private final NotificationCatalogService notificationCatalogService;

    public NotificationGatewayController(NotificationCatalogService notificationCatalogService) {
        this.notificationCatalogService = notificationCatalogService;
    }

    @PostMapping("/send")
    public Mono<NotificationSendResponse> sendNotification(@RequestBody NotificationSendRequest request) {
        return Mono.fromCallable(() -> {
            var template = notificationCatalogService.getTemplate(request.getTemplateCode());
            String subject = applyVariables(template.getSubject(), request.getVariables());
            String body = applyVariables(template.getBody(), request.getVariables());

            log.info("Notification Gateway stub: template={} recipient={} subject={}",
                    request.getTemplateCode(), request.getRecipient(), subject);

            var response = new NotificationSendResponse();
            response.setStatus("ACCEPTED");
            response.setTemplateCode(request.getTemplateCode());
            response.setRecipient(request.getRecipient());
            response.setSubject(subject);
            response.setBody(body);
            return response;
        });
    }

    private String applyVariables(String text, java.util.Map<String, String> variables) {
        if (text == null || variables == null) {
            return text;
        }
        String result = text;
        for (var entry : variables.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
