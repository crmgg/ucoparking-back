package co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.domain;

import java.util.UUID;

public class RegisterNewStudentDomain {

    private UUID id;
    private UUID academicProgram;
    private UUID idType;
    private String idNumber;
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

        // ¿Cómo garantizar que el objeto de dominio se cree de forma integral validado
        // a nivel de tipo de dato, longitud, obligatoriedad, formato, rango sobre cada
        // uno de los atributos involucrados?
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

    private void generateId() {
        this.id = UUID.randomUUID();
    }

    private void setAcademicProgram(UUID academicProgram) {
        this.academicProgram = academicProgram;
    }

    private void setIdType(UUID idType) {
        this.idType = idType;
    }

    private void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}