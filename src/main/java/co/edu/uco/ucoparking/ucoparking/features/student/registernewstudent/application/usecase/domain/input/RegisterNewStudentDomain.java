package co.edu.uco.ucoparking.ucoparking.features.student.registernewstudent.application.usecase.domain.input;

import java.util.UUID;

public class RegisterNewStudentDomain {

    private UUID id;
    private UUID academicProgram;
    private UUID idType;
    private String idTYpe;
    private String email;
    private String mobileNumber;

    public RegisterNewStudentDomain(UUID id, UUID academicProgram, UUID idType, String idTYpe, String email, String mobileNumber) {
       super();
       generateId();
       setAcademicProgram(academicProgram);
       setIdType(idType);
       setIdTYpe(idTYpe);
       setEmail(email);
       setMobileNumber(mobileNumber);

       //¿Como garantizar que el objeto de domini se cree de forma integral a nivel de tipo de dato, longuitud, obligatoriedad, formato, rango, sobre cada uno de los atrubutos involucrados?

    }

    public UUID getId() {
        return id;
    }

    private void generateId() {
        this.id = UUID.randomUUID();
    }

    public UUID getAcademicProgram() {
        return academicProgram;
    }

    private void setAcademicProgram(UUID academicProgram) {
        this.academicProgram = academicProgram;
    }

    public UUID getIdType() {
        return idType;
    }

    private void setIdType(UUID idType) {
        this.idType = idType;
    }

    public String getIdTYpe() {
        return idTYpe;
    }

    private void setIdTYpe(String idTYpe) {
        this.idTYpe = idTYpe;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    private void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
