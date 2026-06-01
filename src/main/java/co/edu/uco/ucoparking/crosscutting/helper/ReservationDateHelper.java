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

    /** Fecha calendario de hoy en Colombia (yyyy-MM-dd). */
    public static boolean isToday(String reservationDate) {
        return reservationDate != null
                && !reservationDate.isBlank()
                && today().equals(reservationDate.trim());
    }

    public static String normalizePlate(String plate) {
        if (plate == null) {
            return null;
        }
        return plate.trim().toUpperCase();
    }
}
