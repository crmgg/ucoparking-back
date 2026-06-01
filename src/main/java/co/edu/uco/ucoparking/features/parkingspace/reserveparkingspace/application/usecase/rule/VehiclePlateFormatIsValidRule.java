package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.crosscutting.exception.UcoParkingException;
import co.edu.uco.ucoparking.crosscutting.helper.ReservationDateHelper;

public class VehiclePlateFormatIsValidRule implements Rule {

    private static final Rule instance = new VehiclePlateFormatIsValidRule();

    private VehiclePlateFormatIsValidRule() {
    }

    public static void executeRule(String vehiclePlate) {
        instance.execute(vehiclePlate);
    }

    @Override
    public void execute(Object... data) {
        String plate = (String) data[0];
        String normalized = ReservationDateHelper.normalizePlate(plate);

        if (normalized == null || !ReservationDateHelper.isValidPlateFormat(normalized)) {
            throw UcoParkingException.create(
                    "Placa invalida. Carro: 3 letras + 3 numeros (ej. ABC123). "
                            + "Moto: 3 letras + 2 numeros + 1 letra (ej. ABC12D).",
                    "vehiclePlate format invalid: " + plate);
        }
    }
}
