package co.edu.uco.ucoparking.infraestructure.service;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.StudentJPARepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentNotificationEmailResolver {

    private static final Logger log = LoggerFactory.getLogger(StudentNotificationEmailResolver.class);

    private final StudentJPARepository studentRepository;

    public StudentNotificationEmailResolver(StudentJPARepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String resolve(String studentEmail, String studentName) {
        if (studentEmail != null && !studentEmail.isBlank()) {
            return studentEmail.trim();
        }

        if (studentName == null || studentName.isBlank()) {
            return null;
        }

        String normalizedName = studentName.trim();

        if (normalizedName.contains("@")) {
            var byEmail = studentRepository.findFirstByEmailIgnoreCase(normalizedName);
            if (byEmail.isPresent()) {
                return byEmail.get().getEmail();
            }
        }

        var byFullName = studentRepository.findFirstByFullNameIgnoreCase(normalizedName);
        if (byFullName.isPresent()) {
            log.debug("Correo de reserva resuelto desde BD por nombre completo: {}", byFullName.get().getEmail());
            return byFullName.get().getEmail();
        }

        String[] parts = normalizedName.split("\\s+");
        if (parts.length >= 2) {
            var match = studentRepository
                    .findFirstByNameIgnoreCaseAndFirstLastNameIgnoreCase(parts[0], parts[1]);
            if (match.isPresent()) {
                log.debug("Correo de reserva resuelto desde BD por nombre: {}", match.get().getEmail());
                return match.get().getEmail();
            }
        }

        var byName = studentRepository.findFirstByNameIgnoreCase(parts[0]);
        if (byName.isPresent()) {
            log.debug("Correo de reserva resuelto desde BD por nombre simple: {}", byName.get().getEmail());
            return byName.get().getEmail();
        }

        log.warn("No se pudo resolver correo para notificacion de reserva (nombre={})", studentName);
        return null;
    }
}
