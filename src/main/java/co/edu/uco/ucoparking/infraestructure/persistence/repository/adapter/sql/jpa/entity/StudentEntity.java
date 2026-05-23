package co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.AcademicProgramJpaEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.IdTypeJpaEntity;

import java.util.UUID;

/**
 * Modelo usado por el adaptador de persistencia. No es entidad JPA: el mapeo ORM de estudiante
 * está en {@link co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.StudentJpaEntity}.
 * Antes estaba mal anotado como {@code @Entity} con {@code @Column} sobre tipos entidad, lo que
 * rompía el arranque de Hibernate.
 */
public class StudentEntity {

    private UUID id;
    private AcademicProgramJpaEntity academicProgram;
    private IdTypeJpaEntity idType;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private String email;
    private String phoneNumber;

    public StudentEntity() {
    }

    public StudentEntity(UUID id, AcademicProgramJpaEntity academicProgram, IdTypeJpaEntity idType, String name,
                         String firstLastName, String secondLastName, String email, String phoneNumber) {
        this.id = id;
        this.academicProgram = academicProgram;
        this.idType = idType;
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AcademicProgramJpaEntity getAcademicProgram() {
        return academicProgram;
    }

    public void setAcademicProgram(AcademicProgramJpaEntity academicProgram) {
        this.academicProgram = academicProgram;
    }

    public IdTypeJpaEntity getIdType() {
        return idType;
    }

    public void setIdType(IdTypeJpaEntity idType) {
        this.idType = idType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
