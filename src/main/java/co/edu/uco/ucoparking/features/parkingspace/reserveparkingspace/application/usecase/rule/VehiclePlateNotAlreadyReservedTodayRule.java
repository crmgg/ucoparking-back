package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;

public class VehiclePlateNotAlreadyReservedTodayRule implements Rule {

    private static final Rule instance = new VehiclePlateNotAlreadyReservedTodayRule();

    private VehiclePlateNotAlreadyReservedTodayRule() {
    }

    public static void executeRule(boolean plateAlreadyReserved) {
        instance.execute(plateAlreadyReserved);
    }

    @Override
    public void execute(Object... data) {
        var plateAlreadyReserved = (Boolean) data[0];

        if (plateAlreadyReserved) {
            throw UcoParkingException.create(
                    "Esta placa ya tiene una reserva activa hoy.",
                    "vehiclePlate already reserved for today");
        }
    }
}
