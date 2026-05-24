package co.edu.uco.ucoparking.infraestructure.controller.catalog.dto;

public class NotificationTemplateResponse {

    private String code;
    private String subject;
    private String body;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
