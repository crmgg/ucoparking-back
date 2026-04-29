package co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.inputport.to.input;

import java.util.UUID;

public class RegisterNewStudentInputTo {

    private UUID id;
    private UUID academicProgram;
    private UUID idType;
    private String idTYpe;
    private String email;
    private String mobileNumber;

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

    public String getIdTYpe() {
        return idTYpe;
    }

    public void setIdTYpe(String idTYpe) {
        this.idTYpe = idTYpe;
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
