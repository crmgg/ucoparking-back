package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

public class ParkingSpaceNumberIsWithinRangeRule implements Rule {

    private static final int MAX_SPACE_NUMBER = 100;
    private static final Rule instance = new ParkingSpaceNumberIsWithinRangeRule();

    private ParkingSpaceNumberIsWithinRangeRule() {
    }

    public static void executeRule(Integer spaceNumber) {
        instance.execute(spaceNumber);
    }

    @Override
    public void execute(Object... data) {
        var spaceNumber = (Integer) data[0];

        if (spaceNumber != null && spaceNumber > MAX_SPACE_NUMBER) {
            String userMessage = "El número de parqueadero no puede ser mayor a " + MAX_SPACE_NUMBER + ".";
            String technicalMessage = "spaceNumber fuera de rango: " + spaceNumber;
            throw UcoParkingException.create(userMessage, technicalMessage);
        }
    }
}
