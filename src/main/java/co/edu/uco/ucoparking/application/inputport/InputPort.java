package co.edu.uco.ucoparking.application.inputport;

public interface InputPort<T, R> {

    R execute(T data);
}
