package co.edu.uco.ucoparking.application.usecase.rule.generics;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.application.usecase.specification.generics.IsMandatorySpecification;
import co.edu.uco.ucoparking.application.usecase.specification.generics.StringFormatValueIsValidSpecification;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

import java.util.UUID;

public class StringFormatValueIsValidRule implements Rule {

    private static final Rule instance = new StringFormatValueIsValidRule();

    private StringFormatValueIsValidRule() {

    }

    public static void executeRule(Object...data ) {
        instance.execute(data);
    }

    @Override
    public void execute(Object... data) {
        if (!new IsMandatorySpecification().isSatisfiedBy(data)) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "Error técnico: el valor proporcionado no tiene el formato de cadena esperado.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

        if (data.length < 4) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "Error técnico: el formato de la cadena no es válido para la operación.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

        var stringData = (String) data[0];
        var dataName = (String) data[1];
        var pattern = (String) data[2];
        boolean mustApplyTrim = (Boolean) data[3];

        var formatSpecification = new StringFormatValueIsValidSpecification(pattern, mustApplyTrim);

        if (!formatSpecification.isSatisfiedBy(stringData)) {
            String userMessage = "El formato del valor ingresado es incorrecto: " + dataName;
            String technicalMessage = "Formato incorrecto para: " + dataName + " con patrón: " + pattern;
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

    }

}
