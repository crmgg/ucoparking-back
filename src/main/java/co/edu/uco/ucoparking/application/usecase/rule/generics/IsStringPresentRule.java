package co.edu.uco.ucoparking.application.usecase.rule.generics;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.application.usecase.specification.generics.IsMandatorySpecification;
import co.edu.uco.ucoparking.application.usecase.specification.generics.IsStringValuePresentSpecification;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

public class IsStringPresentRule implements Rule {
    private static final Rule instance = new IsStringPresentRule();

    private IsStringPresentRule() {
    }

    public static void executeRule(Object... data) {
        instance.execute(data);
    }

    @Override
    public void execute(Object... data) {
        if (!new IsMandatorySpecification().isSatisfiedBy(data)) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "Error técnico: el valor proporcionado no tiene el formato de cadena esperado.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

        if (data.length < 3) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "Error técnico: la longitud de la cadena no es válida para la operación.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

        var stringData = (String) data[0];
        var dataName = (String) data[1];
        boolean mustApplyTrim = (Boolean) data[2];

        var isStringValuePresent = new IsStringValuePresentSpecification(mustApplyTrim);

        if (!isStringValuePresent.isSatisfiedBy(stringData)) {
            String userMessage = "Faltan parámetros requeridos: " + dataName;
            String technicalMessage = "Parámetros incompletos detectados: " + dataName;
            throw UcoParkingException.create(userMessage, technicalMessage);
        }
    }
}
