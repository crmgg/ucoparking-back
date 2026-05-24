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
import co.edu.uco.ucoparking.infraestructure.service.MessageCatalogService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/students")
public class StudentController {

    private final RegisterNewStudentInputPort inputPort;
    private final MessageCatalogService messageCatalogService;

    public StudentController(RegisterNewStudentInputPort inputPort,
                             MessageCatalogService messageCatalogService) {
        this.inputPort = inputPort;
        this.messageCatalogService = messageCatalogService;
    }

    @PostMapping
    public Mono<ResponseEntity<Response<Void>>> registerNewStudent(@RequestBody RegisterNewStudentDTO student) {
        return inputPort.execute(student)
                .then(Mono.fromCallable(() -> {
                    Response<Void> response = Response.createSuccededResponse();
                    response.addMessage(messageCatalogService.getMessage("STUDENT_REGISTERED", "es"));
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                }))
                .onErrorResume(UcoParkingException.class, exception -> {
                    Response<Void> response = Response.createFailedResponse();
                    response.addMessage(exception.getUserMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
                })
                .onErrorResume(Exception.class, exception -> {
                    Response<Void> response = Response.createFailedResponse();
                    response.addMessage("Error al intentar realizar la operación");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }
}
