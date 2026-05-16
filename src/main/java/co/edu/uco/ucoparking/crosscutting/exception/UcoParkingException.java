package co.edu.uco.ucoparking.crosscutting.exception;

import co.edu.uco.ucoparking.crosscutting.helper.ObjectHelper;
import co.edu.uco.ucoparking.crosscutting.helper.TextHelper;

public class UcoParkingException extends RuntimeException {

    private static final long serialVersionUID = -433023700129545247L;

    private Throwable rootException;
    private String userMessage;
    private String technicalMessage;


    private UcoParkingException(final Throwable rootException, final String userMessage, final String technicalMessage) {
        setRootException(rootException);
        setUserMessage(userMessage);
        setTechnicalMessage(technicalMessage);
    }

    public static UcoParkingException create(final String userMessage) {
        return new UcoParkingException(new Exception(), userMessage, userMessage);
    }

    public static UcoParkingException create(final String userMessage, final String technicalMessage) {
        return new UcoParkingException(new Exception(), userMessage, technicalMessage);
    }

    public static UcoParkingException create(final Throwable rootException, final String userMessage, final String technicalMessage) {
        return new UcoParkingException(rootException, userMessage, technicalMessage);
    }

    public Throwable getRootException() {
        return rootException;
    }

    private void setRootException(final Throwable rootException) {
        this.rootException = ObjectHelper.getDefault(rootException, new Exception());
    }

    public String getUserMessage() {
        return userMessage;
    }

    private void setUserMessage(final String userMessage) {
        this.userMessage = TextHelper.getDefaultWithTrim(userMessage);
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }

    private void setTechnicalMessage(final String technicalMessage) {
        this.technicalMessage = TextHelper.getDefaultWithTrim(technicalMessage);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
