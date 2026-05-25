package co.edu.uco.ucoparking.infraestructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    @ConditionalOnProperty(name = "notification.email.enabled", havingValue = "false", matchIfMissing = true)
    public JavaMailSender noopMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("localhost");
        sender.setPort(25);
        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "false");
        return sender;
    }
}
