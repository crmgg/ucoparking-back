package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

public class ParkingSpaceIsAvailableRule implements Rule {

    private static final String AVAILABLE_STATUS = "AVAILABLE";
    private static final Rule instance = new ParkingSpaceIsAvailableRule();

    private ParkingSpaceIsAvailableRule() {
    }

    public static void executeRule(String status, Integer spaceNumber) {
        instance.execute(status, spaceNumber);
    }

    @Override
    public void execute(Object... data) {
        var status = (String) data[0];
        var spaceNumber = (Integer) data[1];

        if (!AVAILABLE_STATUS.equals(status)) {
            String userMessage = "El parqueadero " + spaceNumber + " no está disponible para reservar.";
            String technicalMessage = "Estado actual del parqueadero " + spaceNumber + ": " + status;
            throw UcoParkingException.create(userMessage, technicalMessage);
        }
    }
}
