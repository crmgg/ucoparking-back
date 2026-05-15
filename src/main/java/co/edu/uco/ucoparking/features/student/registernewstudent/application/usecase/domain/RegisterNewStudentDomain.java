package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain;

import java.util.UUID;

public class RegisterNewStudentDomain {

    private UUID id;
    private UUID academicProgram;
    private UUID idType;
    private String idNumber ;
    private String email;
    private String mobileNumber;

    public RegisterNewStudentDomain(UUID academicProgram, UUID idType, String idNumber, String email,
                                    String mobileNumber) {
        super();
        generateId();
        setAcademicProgram(academicProgram);
        setIdType(idType);
        setIdNumber(idNumber);
        setEmail(email);
        setMobileNumber(mobileNumber);
    }

    // Validaciones de integridad y formato en los setters:
    private void setAcademicProgram(UUID academicProgram) {
        if (academicProgram == null) {
            throw new IllegalArgumentException("El programa académico es obligatorio");
        }
        this.academicProgram = academicProgram;
    }

    private void setIdType(UUID idType) {
        if (idType == null) {
            throw new IllegalArgumentException("El tipo de identificación es obligatorio");
        }
        this.idType = idType;
    }

    private void setIdNumber(String idNumber) {
        if (idNumber == null || idNumber.isBlank()) {
            throw new IllegalArgumentException("El número de identificación es obligatorio");
        }
        if (idNumber.length() < 5 || idNumber.length() > 20) {
            throw new IllegalArgumentException("El número de identificación debe tener entre 5 y 20 caracteres");
        }
        this.idNumber = idNumber;
    }

    private void setEmail(String email) {
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
        this.email = email;
    }

    private void setMobileNumber(String mobileNumber) {
        if (mobileNumber == null || !mobileNumber.matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("El número de celular debe tener 10 dígitos");
        }
        this.mobileNumber = mobileNumber;
    }

    private void generateId() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
    public UUID getAcademicProgram() {
        return academicProgram;
    }
    public UUID getIdType() {
        return idType;
    }
    public String getIdNumber() {
        return idNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }
}