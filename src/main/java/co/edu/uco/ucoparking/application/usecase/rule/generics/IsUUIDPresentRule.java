package co.edu.uco.ucoparking.application.usecase.rule.generics;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.application.usecase.specification.generics.IsMandatorySpecification;
import co.edu.uco.ucoparking.application.usecase.specification.generics.IsUUIDPresentSpecification;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

import java.util.UUID;

public class IsUUIDPresentRule implements Rule {

    private static final Rule instance = new IsUUIDPresentRule();

    private IsUUIDPresentRule() {

    }

    public static void executeRule(Object...data) {
        instance.execute(data);
    }

    @Override
    public void execute(Object... data) {
        if(!new IsMandatorySpecification().isSatisfiedBy(data)) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "Error técnico: el valor proporcionado no tiene el formato de UUID esperado.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

        if (data.length < 2) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "Error técnico: la longitud de los datos para UUID no es válida.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }

        var uuidData = (UUID) data [0];
        var dataName = (String) data [1];

        var isUUIDPresentSpecification = new IsUUIDPresentSpecification();

        if(!isUUIDPresentSpecification.isSatisfiedBy(uuidData)) {
            String userMessage = "Ocurrió un error al intentar realizar la operación.";
            String technicalMessage = "El UUID proporcionado es inválido o por defecto: " + dataName;
            throw UcoParkingException.create(userMessage, technicalMessage);
        }
    }

}
