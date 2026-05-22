package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain;

import java.util.UUID;

public class ReserveParkingSpaceDomain {

    private UUID id;
    private Integer spaceNumber;
    private UUID studentId;
    private String status;

    public ReserveParkingSpaceDomain(Integer spaceNumber, UUID studentId, String status) {
        super();
        generateId();
        setSpaceNumber(spaceNumber);
        setStudentId(studentId);
        setStatus(status);
    }

    // Validaciones de integridad y formato en los setters:
    private void setSpaceNumber(Integer spaceNumber) {
        if (spaceNumber == null || spaceNumber <= 0) {
            throw new IllegalArgumentException("El número de espacio es obligatorio y debe ser mayor a 0");
        }
        this.spaceNumber = spaceNumber;
    }

    private void setStudentId(UUID studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("El ID del estudiante es obligatorio");
        }
        this.studentId = studentId;
    }

    private void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        this.status = status;
    }

    private void generateId() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public Integer getSpaceNumber() {
        return spaceNumber;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public String getStatus() {
        return status;
    }
}
