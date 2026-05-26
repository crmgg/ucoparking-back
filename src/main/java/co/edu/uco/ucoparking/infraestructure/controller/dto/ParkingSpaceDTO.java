package co.edu.uco.ucoparking.infraestructure.controller.dto;

public class ParkingSpaceDTO {

    private String id;
    private Integer spaceNumber;
    private String status;
    private String occupiedByStudentId;
    private String occupiedByStudentName;
    private String vehiclePlate;
    private String reservationStartTime;
    private String reservationEndTime;
    private String reservationDate;
    private Long createdAt;
    private Long updatedAt;

    public ParkingSpaceDTO() {
    }

    public ParkingSpaceDTO(String id, Integer spaceNumber, String status,
                          String occupiedByStudentId, String occupiedByStudentName,
                          String vehiclePlate, String reservationStartTime,
                          String reservationEndTime, String reservationDate,
                          Long createdAt, Long updatedAt) {
        this.id = id;
        this.spaceNumber = spaceNumber;
        this.status = status;
        this.occupiedByStudentId = occupiedByStudentId;
        this.occupiedByStudentName = occupiedByStudentName;
        this.vehiclePlate = vehiclePlate;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
        this.reservationDate = reservationDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(Integer spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOccupiedByStudentId() {
        return occupiedByStudentId;
    }

    public void setOccupiedByStudentId(String occupiedByStudentId) {
        this.occupiedByStudentId = occupiedByStudentId;
    }

    public String getOccupiedByStudentName() {
        return occupiedByStudentName;
    }

    public void setOccupiedByStudentName(String occupiedByStudentName) {
        this.occupiedByStudentName = occupiedByStudentName;
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

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
