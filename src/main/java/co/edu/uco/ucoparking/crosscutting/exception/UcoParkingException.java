package co.edu.uco.ucoparking.crosscutting.exception;

public class UcoParkingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UcoParkingException(String message) {
        super(message);
    }

    public UcoParkingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UcoParkingException(Throwable cause) {
        super(cause);
    }
}
