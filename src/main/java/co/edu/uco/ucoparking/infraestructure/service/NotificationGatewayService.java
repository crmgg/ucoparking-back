package co.edu.uco.ucoparking.infraestructure.service;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationSendRequest;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationGatewayService {

    private static final Logger log = LoggerFactory.getLogger(NotificationGatewayService.class);

    private final NotificationCatalogService notificationCatalogService;
    private final JavaMailSender mailSender;

    @Value("${notification.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${notification.email.from:}")
    private String fromEmail;

    public NotificationGatewayService(NotificationCatalogService notificationCatalogService,
                                      JavaMailSender mailSender) {
        this.notificationCatalogService = notificationCatalogService;
        this.mailSender = mailSender;
    }

    public Mono<NotificationSendResponse> send(NotificationSendRequest request) {
        return Mono.fromCallable(() -> sendSync(request))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> sendReservationConfirmed(String recipient, String studentName, Integer spaceNumber) {
        if (recipient == null || recipient.isBlank()) {
            return Mono.empty();
        }

        var request = new NotificationSendRequest();
        request.setTemplateCode("RESERVATION_CONFIRMED");
        request.setRecipient(recipient);
        request.setChannel("EMAIL");
        request.setVariables(Map.of(
                "studentName", studentName != null ? studentName : "Estudiante",
                "spaceNumber", String.valueOf(spaceNumber)
        ));

        return send(request)
                .doOnError(error -> log.warn("No se pudo enviar correo de reserva: {}", error.getMessage()))
                .onErrorComplete()
                .then();
    }

    public Mono<Void> sendWelcome(String recipient, String studentName) {
        if (recipient == null || recipient.isBlank()) {
            return Mono.empty();
        }

        var request = new NotificationSendRequest();
        request.setTemplateCode("WELCOME_STUDENT");
        request.setRecipient(recipient);
        request.setChannel("EMAIL");
        request.setVariables(Map.of(
                "studentName", studentName != null ? studentName : "Estudiante"
        ));

        return send(request)
                .doOnError(error -> log.warn("No se pudo enviar correo de bienvenida: {}", error.getMessage()))
                .onErrorComplete()
                .then();
    }

    private NotificationSendResponse sendSync(NotificationSendRequest request) {
        validateRequest(request);

        var template = notificationCatalogService.getTemplate(request.getTemplateCode());
        Map<String, String> variables = request.getVariables() != null
                ? request.getVariables()
                : new HashMap<>();

        String subject = applyVariables(template.getSubject(), variables);
        String body = applyVariables(template.getBody(), variables);
        String channel = normalizeChannel(request.getChannel());

        var response = new NotificationSendResponse();
        response.setTemplateCode(request.getTemplateCode());
        response.setRecipient(request.getRecipient());
        response.setSubject(subject);
        response.setBody(body);
        response.setChannel(channel);

        if ("SMS".equals(channel)) {
            response.setStatus("UNSUPPORTED");
            response.setDetail("SMS no configurado. Usa channel=EMAIL con SMTP.");
            log.warn("SMS solicitado pero no hay proveedor configurado para {}", request.getRecipient());
            return response;
        }

        if (!emailEnabled) {
            response.setStatus("SKIPPED");
            response.setDetail("Correo desactivado. Activa NOTIFICATION_EMAIL_ENABLED=true y SMTP_*.");
            log.info("Notification Gateway (email desactivado): to={} subject={}", request.getRecipient(), subject);
            return response;
        }

        if (fromEmail == null || fromEmail.isBlank()) {
            throw UcoParkingException.create(
                    "Correo no configurado",
                    "Falta notification.email.from / NOTIFICATION_FROM_EMAIL");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(request.getRecipient());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);

        response.setStatus("SENT");
        response.setDetail("Correo enviado via SMTP");
        log.info("Notification Gateway email enviado: to={} template={}", request.getRecipient(), request.getTemplateCode());
        return response;
    }

    private void validateRequest(NotificationSendRequest request) {
        if (request.getTemplateCode() == null || request.getTemplateCode().isBlank()) {
            throw UcoParkingException.create("templateCode es obligatorio", "templateCode vacio");
        }
        if (request.getRecipient() == null || request.getRecipient().isBlank()) {
            throw UcoParkingException.create("recipient es obligatorio", "recipient vacio");
        }
    }

    private String normalizeChannel(String channel) {
        if (channel == null || channel.isBlank()) {
            return "EMAIL";
        }
        return channel.trim().toUpperCase();
    }

    private String applyVariables(String text, Map<String, String> variables) {
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
