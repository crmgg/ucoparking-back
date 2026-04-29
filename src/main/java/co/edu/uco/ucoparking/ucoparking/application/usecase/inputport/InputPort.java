package co.edu.uco.ucoparking.ucoparking.application.usecase.inputport;

public interface InputPort <T, R> {

    R execute(T data);
}
