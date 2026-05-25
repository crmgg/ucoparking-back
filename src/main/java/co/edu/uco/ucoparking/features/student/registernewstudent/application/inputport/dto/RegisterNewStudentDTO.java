package co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto;

import java.util.UUID;

public class RegisterNewStudentDTO {
    private UUID id;
    private UUID academicProgram;
    private UUID idType;
    private String idNumber;
    private String name;
    private String email;
    private String mobileNumber;

    public RegisterNewStudentDTO() {
    }

    public RegisterNewStudentDTO(UUID id, UUID academicProgram, UUID idType,
                                 String idNumber, String name, String email, String mobileNumber) {
        this.id = id;
        this.academicProgram = academicProgram;
        this.idType = idType;
        this.idNumber = idNumber;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAcademicProgram() {
        return academicProgram;
    }

    public void setAcademicProgram(UUID academicProgram) {
        this.academicProgram = academicProgram;
    }

    public UUID getIdType() {
        return idType;
    }

    public void setIdType(UUID idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
