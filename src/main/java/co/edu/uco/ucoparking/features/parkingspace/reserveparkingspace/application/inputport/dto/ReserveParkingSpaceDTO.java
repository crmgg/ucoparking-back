package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto;

import java.util.UUID;

public class ReserveParkingSpaceDTO {

    private UUID id;
    private Integer spaceNumber;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String status;

    public ReserveParkingSpaceDTO() {
    }

    public ReserveParkingSpaceDTO(Integer spaceNumber, String studentId, String studentName, String status) {
        this.spaceNumber = spaceNumber;
        this.studentId = studentId;
        this.studentName = studentName;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(Integer spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
