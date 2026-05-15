package co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto;

import java.util.UUID;

public class RegisterNewStudentDTO {
    private UUID id;
    private UUID academicProgram;
    private UUID idType;
    private String idNumber;
    private String email;
    private String mobileNumber;

    public RegisterNewStudentDTO(UUID id, UUID academicProgram, UUID idType,
                                 String idNumber, String email, String mobileNumber) {
        super();
        setId(id);
        setAcademicProgram(academicProgram);
        setIdType(idType);
        setIdNumber(idNumber);
        setEmail(email);
        setMobileNumber(mobileNumber);
    }

    private void setId(UUID id) {
        this.id = id;
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
