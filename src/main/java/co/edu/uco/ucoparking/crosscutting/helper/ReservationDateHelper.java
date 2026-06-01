package co.edu.uco.ucoparking.crosscutting.helper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Pattern;

public final class ReservationDateHelper {

    private static final ZoneId ZONE = ZoneId.of("America/Bogota");
    private static final Pattern PLATE_PATTERN = Pattern.compile("^[A-Z]{3}([0-9]{3}|[0-9]{2}[A-Z])$");

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

    /** Carro: ABC123. Moto: ABC12D. */
    public static boolean isValidPlateFormat(String normalizedPlate) {
        return normalizedPlate != null && PLATE_PATTERN.matcher(normalizedPlate).matches();
    }
}
