package co.edu.uco.ucoparking.infraestructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_spaces")
@org.springframework.data.relational.core.mapping.Table("parking_spaces")
public class ParkingSpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @org.springframework.data.annotation.Id
    @jakarta.persistence.Column(name = "id")
    @org.springframework.data.relational.core.mapping.Column("id")
    private String id;

    @jakarta.persistence.Column(name = "space_number", nullable = false, unique = true)
    @org.springframework.data.relational.core.mapping.Column("space_number")
    private Integer spaceNumber;

    @jakarta.persistence.Column(name = "status", nullable = false)
    @org.springframework.data.relational.core.mapping.Column("status")
    private String status;

    @jakarta.persistence.Column(name = "occupied_by_student_id")
    @org.springframework.data.relational.core.mapping.Column("occupied_by_student_id")
    private String occupiedByStudentId;

    @jakarta.persistence.Column(name = "occupied_by_student_name")
    @org.springframework.data.relational.core.mapping.Column("occupied_by_student_name")
    private String occupiedByStudentName;

    @jakarta.persistence.Column(name = "created_at", nullable = false)
    @org.springframework.data.relational.core.mapping.Column("created_at")
    private Long createdAt;

    @jakarta.persistence.Column(name = "updated_at", nullable = false)
    @org.springframework.data.relational.core.mapping.Column("updated_at")
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
