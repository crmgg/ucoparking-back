package co.edu.uco.ucoparking.crosscutting.exception;

import co.edu.uco.ucoparking.infraestructure.controller.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UcoParkingException.class)
    public Mono<ResponseEntity<Response<Void>>> handleUcoParkingException(UcoParkingException exception) {
        Response<Void> response = Response.createFailedResponse();
        response.addMessage(exception.getUserMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Response<Void>>> handleIllegalArgumentException(IllegalArgumentException exception) {
        Response<Void> response = Response.createFailedResponse();
        response.addMessage(exception.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }

    @ExceptionHandler(IllegalStateException.class)
    public Mono<ResponseEntity<Response<Void>>> handleIllegalStateException(IllegalStateException exception) {
        Response<Void> response = Response.createFailedResponse();
        response.addMessage(exception.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(response));
    }
}
