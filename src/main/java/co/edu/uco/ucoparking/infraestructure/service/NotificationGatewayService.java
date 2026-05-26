package co.edu.uco.ucoparking.infraestructure.service;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationSendRequest;
import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.NotificationSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
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
    private final ResendEmailSender resendEmailSender;

    @Value("${notification.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${notification.email.provider:smtp}")
    private String emailProvider;

    @Value("${notification.email.from:}")
    private String fromEmail;

    public NotificationGatewayService(NotificationCatalogService notificationCatalogService,
                                      JavaMailSender mailSender,
                                      ResendEmailSender resendEmailSender) {
        this.notificationCatalogService = notificationCatalogService;
        this.mailSender = mailSender;
        this.resendEmailSender = resendEmailSender;
    }

    public Mono<NotificationSendResponse> send(NotificationSendRequest request) {
        return Mono.fromCallable(() -> sendSync(request))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> sendReservationConfirmed(
            String recipient,
            String studentName,
            Integer spaceNumber,
            String startTime,
            String endTime,
            String vehiclePlate) {
        if (recipient == null || recipient.isBlank()) {
            log.warn("Reserva sin studentEmail: no se envia correo de confirmacion");
            return Mono.empty();
        }

        var request = new NotificationSendRequest();
        request.setTemplateCode("RESERVATION_CONFIRMED");
        request.setRecipient(recipient);
        request.setChannel("EMAIL");
        request.setVariables(Map.of(
                "studentName", studentName != null ? studentName : "Estudiante",
                "spaceNumber", String.valueOf(spaceNumber),
                "startTime", startTime != null ? startTime : "—",
                "endTime", endTime != null ? endTime : "—",
                "vehiclePlate", vehiclePlate != null ? vehiclePlate : "—"
        ));

        return send(request)
                .doOnNext(response -> {
                    if ("SENT".equals(response.getStatus())) {
                        log.info("Correo de reserva enviado a {}", recipient);
                    } else {
                        log.warn("Correo de reserva no enviado: status={} detail={}",
                                response.getStatus(), response.getDetail());
                    }
                })
                .doOnError(error -> log.error("No se pudo enviar correo de reserva a {}: {}", recipient, error.getMessage()))
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
            response.setDetail("Correo desactivado. Activa NOTIFICATION_EMAIL_ENABLED=true.");
            log.warn("Notification Gateway (email desactivado): to={} template={}", request.getRecipient(), request.getTemplateCode());
            return response;
        }

        if (fromEmail == null || fromEmail.isBlank()) {
            throw UcoParkingException.create(
                    "Correo no configurado",
                    "Falta notification.email.from / NOTIFICATION_FROM_EMAIL");
        }

        if (usesResend()) {
            return sendViaResend(request, response, subject, body);
        }

        return sendViaSmtp(request, response, subject, body);
    }

    private boolean usesResend() {
        return "resend".equalsIgnoreCase(emailProvider);
    }

    private NotificationSendResponse sendViaResend(
            NotificationSendRequest request,
            NotificationSendResponse response,
            String subject,
            String body) {
        if (!resendEmailSender.isConfigured()) {
            response.setStatus("FAILED");
            response.setDetail("Falta RESEND_API_KEY. Crea una en https://resend.com/api-keys");
            return response;
        }

        try {
            resendEmailSender.sendEmail(fromEmail, request.getRecipient(), subject, body);
        } catch (RuntimeException ex) {
            response.setStatus("FAILED");
            response.setDetail(ex.getMessage());
            log.error("Notification Gateway Resend fallo: to={} template={} cause={}",
                    request.getRecipient(), request.getTemplateCode(), ex.getMessage());
            return response;
        }

        response.setStatus("SENT");
        response.setDetail("Correo enviado via Resend");
        log.info("Notification Gateway email enviado (Resend): to={} template={}",
                request.getRecipient(), request.getTemplateCode());
        return response;
    }

    private NotificationSendResponse sendViaSmtp(
            NotificationSendRequest request,
            NotificationSendResponse response,
            String subject,
            String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(request.getRecipient());
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            response.setStatus("FAILED");
            response.setDetail(buildMailErrorDetail(ex));
            log.error("Notification Gateway email fallo: to={} template={} cause={}",
                    request.getRecipient(), request.getTemplateCode(), ex.getMessage());
            return response;
        }

        response.setStatus("SENT");
        response.setDetail("Correo enviado via SMTP");
        log.info("Notification Gateway email enviado: to={} template={}", request.getRecipient(), request.getTemplateCode());
        return response;
    }

    private String buildMailErrorDetail(MailException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        Throwable cause = ex.getCause();
        while (cause != null) {
            if (cause.getMessage() != null) {
                message = message + " " + cause.getMessage();
            }
            cause = cause.getCause();
        }
        if (message.contains("535")
                || message.contains("Authentication")
                || message.contains("BadCredentials")) {
            return "SMTP rechazo usuario/contrasena. Usa Resend (NOTIFICATION_EMAIL_PROVIDER=resend) "
                    + "o renueva SMTP_PASSWORD en infisical-secrets.env.";
        }
        return "Error SMTP: " + message.trim();
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
