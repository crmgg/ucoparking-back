package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

public class StudentDoesNotHaveActiveParkingSpaceRule implements Rule {

    private static final Rule instance = new StudentDoesNotHaveActiveParkingSpaceRule();

    private StudentDoesNotHaveActiveParkingSpaceRule() {
    }

    public static void executeRule(boolean hasActiveSpace) {
        instance.execute(hasActiveSpace);
    }

    @Override
    public void execute(Object... data) {
        var hasActiveSpace = (Boolean) data[0];

        if (hasActiveSpace) {
            String userMessage = "El estudiante ya tiene un parqueadero reservado.";
            String technicalMessage = "El estudiante ya ocupa un espacio de parqueadero activo.";
            throw UcoParkingException.create(userMessage, technicalMessage);
        }
    }
}
