package co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Estudiante")
public class StudentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "programa_academico", nullable = false)
    private AcademicProgramJpaEntity academicProgram; //Preguntar farid

    @ManyToOne
    @JoinColumn(name = "tipo_identificacion", nullable = false)
    private IdTypeJpaEntity idType; //Preguntar farid

    @Column(name = "nombre", length = 50, nullable = false)
    private String name;

    @Column(name = " Primer apellido", length = 50, nullable = false)
    private  String firstLastName;

    @Column(name = " Segundo apellido", length = 50, nullable = true)
    private String secondLastName;

    @Column(name = "Correo electronico", length = 100, nullable = false)
    private String email;

    @Column(name = "Numero telefonico", length = 15, nullable = false)
    private String phoneNumber;

    protected StudentJpaEntity() {
    }

    public StudentJpaEntity(UUID id, AcademicProgramJpaEntity academicProgram, IdTypeJpaEntity idType, String name,
                            String firstLastName, String secondLastName, String email, String phoneNumber) {
        super();
        setId(id);
        setAcademicProgram(academicProgram);
        setIdType(idType);
        setName(name);
        setFirstLastName(firstLastName);
        setSecondLastName(secondLastName);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    private void setId(UUID id) {
        this.id = id;
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
