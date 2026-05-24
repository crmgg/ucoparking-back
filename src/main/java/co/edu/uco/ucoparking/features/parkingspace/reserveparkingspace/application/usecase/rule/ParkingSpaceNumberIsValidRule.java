package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

public class ParkingSpaceNumberIsValidRule implements Rule {

    private static final Rule instance = new ParkingSpaceNumberIsValidRule();

    private ParkingSpaceNumberIsValidRule() {
    }

    public static void executeRule(Integer spaceNumber) {
        instance.execute(spaceNumber);
    }

    @Override
    public void execute(Object... data) {
        var spaceNumber = (Integer) data[0];

        if (spaceNumber == null || spaceNumber <= 0) {
            String userMessage = "El número de parqueadero es obligatorio y debe ser mayor a 0.";
            String technicalMessage = "spaceNumber inválido: " + spaceNumber;
            throw UcoParkingException.create(userMessage, technicalMessage);
        }
    }
}
