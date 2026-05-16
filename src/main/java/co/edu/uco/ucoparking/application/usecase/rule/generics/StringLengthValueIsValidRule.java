package co.edu.uco.ucoparking.application.usecase.rule.generics;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.application.usecase.specification.generics.IsMandatorySpecification;
import co.edu.uco.ucoparking.application.usecase.specification.generics.StringLengthSpecification;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

public class StringLengthValueIsValidRule implements Rule {

    private static final Rule instance = new StringLengthValueIsValidRule();

    private StringLengthValueIsValidRule() {}

    public static void executeRule(final Object... data) {
        instance.execute(data);
    }

    @Override
    public void execute(final Object... data) {
        if (!new IsMandatorySpecification().isSatisfiedBy(data)) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "Error técnico: la longitud de la cadena no es válida.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

        if (data.length < 5) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "Error técnico: los parámetros de longitud de cadena son insuficientes.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

        var stringData = (String) data[0];
        var dataName = (String) data[1];
        var minLength = (Integer) data[2];
        var maxLength = (Integer) data[3];
        boolean mustApplyTrim = (Boolean) data[4];

        var lengthSpecification = new StringLengthSpecification(minLength, maxLength, mustApplyTrim);

        if (!lengthSpecification.isSatisfiedBy(stringData)) {
            String userMessage = "La longitud del valor ingresado es incorrecta para: " + dataName + ". Debe estar entre " + minLength + " y " + maxLength + ".";
            String technicalMessage = "Longitud incorrecta para: " + dataName + ". Debe estar entre " + minLength + " y " + maxLength + ".";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }
    }
}