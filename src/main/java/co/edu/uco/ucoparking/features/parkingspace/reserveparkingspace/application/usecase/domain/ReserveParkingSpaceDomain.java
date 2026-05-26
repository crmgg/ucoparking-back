package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.domain;

import java.util.UUID;

public class ReserveParkingSpaceDomain {

    private UUID id;
    private Integer spaceNumber;
    private String studentId;
    private String studentName;
    private String status;
    private String vehiclePlate;
    private String reservationStartTime;
    private String reservationEndTime;

    public ReserveParkingSpaceDomain() {
    }

    public ReserveParkingSpaceDomain(Integer spaceNumber, String studentId, String studentName, String status) {
        this.id = UUID.randomUUID();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public String getReservationStartTime() {
        return reservationStartTime;
    }

    public void setReservationStartTime(String reservationStartTime) {
        this.reservationStartTime = reservationStartTime;
    }

    public String getReservationEndTime() {
        return reservationEndTime;
    }

    public void setReservationEndTime(String reservationEndTime) {
        this.reservationEndTime = reservationEndTime;
    }
}
