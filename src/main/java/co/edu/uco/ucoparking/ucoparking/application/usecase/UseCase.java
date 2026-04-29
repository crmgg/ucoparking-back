package co.edu.uco.ucoparking.ucoparking.application.usecase;

public interface UseCase <D, R> {

    R execute(D data);
}
