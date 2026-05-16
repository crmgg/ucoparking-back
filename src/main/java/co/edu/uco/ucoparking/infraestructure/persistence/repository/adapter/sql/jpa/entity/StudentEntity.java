package co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.AcademicProgramJpaEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.IdTypeJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class StudentEntity {
    @Id
    private UUID id;

    // Longitud y restricciones de la base de datos
    @Column(length = 36, nullable = false)
    private AcademicProgramJpaEntity academicProgram; // Relación, longitud UUID

    @Column(length = 36, nullable = false)
    private IdTypeJpaEntity idType; // Relación, longitud UUID

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String firstLastName;

    @Column(length = 50)
    private String secondLastName;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 15, nullable = false)
    private String phoneNumber;

    private void setId(UUID id) {
        this.id = id;
    }

    public StudentEntity(UUID id, AcademicProgramJpaEntity academicProgram, IdTypeJpaEntity idType, String name, String firstLastName,
                         String secondLastName, String email, String phoneNumber) {
        this.id = id;
        this.academicProgram = academicProgram;
        this.idType = idType;
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    private void setAcademicProgram(AcademicProgramJpaEntity academicProgram) {
        this.academicProgram = academicProgram;
    }

    private void setIdType(IdTypeJpaEntity idType) {
        this.idType = idType;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    private void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public AcademicProgramJpaEntity getAcademicProgram() {
        return academicProgram;
    }

    public IdTypeJpaEntity getIdType() {
        return idType;
    }

    public String getName() {
        return name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
