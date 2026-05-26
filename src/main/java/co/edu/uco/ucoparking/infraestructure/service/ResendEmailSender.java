package co.edu.uco.ucoparking.infraestructure.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Component
public class ResendEmailSender {

    private final WebClient webClient;
    private final String apiKey;

    public ResendEmailSender(@Value("${notification.resend.api-key:}") String apiKey) {
        this.apiKey = apiKey != null ? apiKey.trim() : "";
        this.webClient = WebClient.builder()
                .baseUrl("https://api.resend.com")
                .build();
    }

    public boolean isConfigured() {
        return !apiKey.isBlank();
    }

    public void sendEmail(String from, String to, String subject, String text) {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "Falta RESEND_API_KEY en infisical-secrets.env (https://resend.com/api-keys)");
        }

        Map<String, Object> payload = Map.of(
                "from", from,
                "to", List.of(to),
                "subject", subject,
                "text", text
        );

        try {
            webClient.post()
                    .uri("/emails")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException ex) {
            String detail = ex.getResponseBodyAsString();
            if (detail == null || detail.isBlank()) {
                detail = ex.getMessage();
            }
            throw new IllegalStateException("Resend: " + detail, ex);
        }
    }
}
