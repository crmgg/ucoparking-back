package co.edu.uco.ucoparking.crosscutting.helper;

import java.time.LocalDate;
import java.time.ZoneId;

public final class ReservationDateHelper {

    private static final ZoneId ZONE = ZoneId.of("America/Bogota");

    private ReservationDateHelper() {
    }

    public static String today() {
        return LocalDate.now(ZONE).toString();
    }

    public static String normalizePlate(String plate) {
        if (plate == null) {
            return null;
        }
        return plate.trim().toUpperCase();
    }
}
