package co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.application.usecase.rule.generics.StringLengthValueIsValidRule;

public class StudentNameLengthValueIsValidRule implements Rule {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;
    private static final Rule instance = new StudentNameLengthValueIsValidRule();

    private StudentNameLengthValueIsValidRule() {
    }

    public static void executeRule(String studentName) {
        instance.execute(studentName);
    }

    @Override
    public void execute(Object... data) {
        StringLengthValueIsValidRule.executeRule(
                (String) data[0],
                "nombre del estudiante",
                MIN_LENGTH,
                MAX_LENGTH,
                true
        );
    }
}
