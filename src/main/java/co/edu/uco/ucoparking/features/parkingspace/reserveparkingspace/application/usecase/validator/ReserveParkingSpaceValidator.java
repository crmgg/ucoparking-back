package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.validator;

import co.edu.uco.ucoparking.application.usecase.rule.generics.IsStringPresentRule;
import co.edu.uco.ucoparking.application.usecase.validator.Validator;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.ParkingSpaceNumberIsValidRule;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.ParkingSpaceNumberIsWithinRangeRule;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.VehiclePlateFormatIsValidRule;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule.StudentNameLengthValueIsValidRule;
import org.springframework.stereotype.Component;

@Component
public class ReserveParkingSpaceValidator implements Validator {

    @Override
    public void validate(Object... data) {
        var dto = (ReserveParkingSpaceDTO) data[0];

        ParkingSpaceNumberIsValidRule.executeRule(dto.getSpaceNumber());
        ParkingSpaceNumberIsWithinRangeRule.executeRule(dto.getSpaceNumber());
        IsStringPresentRule.executeRule(dto.getStudentId(), "estudiante", true);
        IsStringPresentRule.executeRule(dto.getStudentName(), "nombre del estudiante", true);
        IsStringPresentRule.executeRule(dto.getVehiclePlate(), "placa del vehículo", true);
        VehiclePlateFormatIsValidRule.executeRule(dto.getVehiclePlate());
        StudentNameLengthValueIsValidRule.executeRule(dto.getStudentName());
    }
}
