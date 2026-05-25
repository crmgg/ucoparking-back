package co.edu.uco.ucoparking.infraestructure.controller.catalog;

import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationSendRequest;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationSendResponse;
import co.edu.uco.ucoparking.infraestructure.service.NotificationGatewayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/notifications")
public class NotificationGatewayController {

    private final NotificationGatewayService notificationGatewayService;

    public NotificationGatewayController(NotificationGatewayService notificationGatewayService) {
        this.notificationGatewayService = notificationGatewayService;
    }

    @PostMapping("/send")
    public Mono<NotificationSendResponse> sendNotification(@RequestBody NotificationSendRequest request) {
        return notificationGatewayService.send(request);
    }
}
