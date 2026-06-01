package co.edu.uco.ucoparking.crosscutting.helper;

import co.edu.uco.ucoparking.infraestructure.persistence.entity.ParkingSpaceEntity;

public final class ParkingSpaceReservationHelper {

    private static final String AVAILABLE_STATUS = "AVAILABLE";
    private static final String OCCUPIED_STATUS = "OCCUPIED";

    private ParkingSpaceReservationHelper() {
    }

    public static boolean isActiveToday(ParkingSpaceEntity space) {
        return space != null
                && OCCUPIED_STATUS.equals(space.getStatus())
                && ReservationDateHelper.isToday(space.getReservationDate());
    }

    public static void clearReservation(ParkingSpaceEntity space) {
        space.setStatus(AVAILABLE_STATUS);
        space.setOccupiedByStudentId(null);
        space.setOccupiedByStudentName(null);
        space.setVehiclePlate(null);
        space.setReservationStartTime(null);
        space.setReservationEndTime(null);
        space.setReservationDate(null);
        space.setUpdatedAt(System.currentTimeMillis());
    }
}
