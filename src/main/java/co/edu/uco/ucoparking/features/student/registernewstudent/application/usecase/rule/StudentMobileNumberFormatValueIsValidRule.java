package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.application.usecase.rule.generics.StringFormatValueIsValidRule;

public class StudentMobileNumberFormatValueIsValidRule implements Rule {

    private static final String MOBILE_PATTERN = "^[0-9]{10}$";
    private static final Rule instance = new StudentMobileNumberFormatValueIsValidRule();

    private StudentMobileNumberFormatValueIsValidRule() {
    }

    public static void executeRule(String mobileNumber) {
        instance.execute(mobileNumber);
    }

    @Override
    public void execute(Object... data) {
        StringFormatValueIsValidRule.executeRule(
                (String) data[0],
                "número de celular",
                MOBILE_PATTERN,
                true
        );
    }
}
