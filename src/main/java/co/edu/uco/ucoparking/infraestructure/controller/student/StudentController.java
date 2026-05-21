package co.edu.uco.ucoparking.infraestructure.controller.student;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.RegisterNewStudentInputPort;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import co.edu.uco.ucoparking.infraestructure.controller.Response;

@RestController
@RequestMapping("/uco-parking/v1/students")
public class StudentController {

    private final RegisterNewStudentInputPort inputPort;

    public StudentController(RegisterNewStudentInputPort inputPort) {
        this.inputPort = inputPort;
    }

    @PostMapping
    public ResponseEntity<Response<Void>> registerNewStudent(@RequestBody RegisterNewStudentDTO student) {

        Response<Void> responseObjectData = Response.createSuccededResponse();
        HttpStatus responseStatusCode = HttpStatus.CREATED;

        try {

            inputPort.execute(student);

            responseObjectData.addMessage("Estudiante registrado exitosamente");
            responseStatusCode = HttpStatus.CREATED;

        } catch (final UcoParkingException exception) {
            responseObjectData = Response.createFailedResponse();
            responseObjectData.addMessage(exception.getUserMessage());
            responseStatusCode = HttpStatus.BAD_REQUEST;

        } catch (final Exception exception) {
            responseObjectData = Response.createFailedResponse();
            responseObjectData.addMessage("Error al intentar realizar la operación");
            responseStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
            exception.printStackTrace();
        }

        return new ResponseEntity<>(responseObjectData, responseStatusCode);
    }

}
