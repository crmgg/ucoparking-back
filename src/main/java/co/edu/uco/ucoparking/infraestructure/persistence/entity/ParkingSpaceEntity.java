package co.edu.uco.ucoparking.infraestructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ParkingSpaces")
public class ParkingSpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private Integer spaceNumber;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private String occupiedByStudentId;

    @Column(nullable = true)
    private String occupiedByStudentName;

    @Column(nullable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long updatedAt;

    public ParkingSpaceEntity() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
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
        this.updatedAt = System.currentTimeMillis();
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
