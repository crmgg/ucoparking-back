package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.rule;

import co.edu.uco.ucoparking.application.usecase.rule.Rule;
import co.edu.uco.ucoparking.application.usecase.rule.generics.StringFormatValueIsValidRule;

public class StudentEmailFormatValueIsValidRule implements Rule {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Rule instance = new StudentEmailFormatValueIsValidRule();

    private StudentEmailFormatValueIsValidRule() {
    }

    public static void executeRule(String email) {
        instance.execute(email);
    }

    @Override
    public void execute(Object... data) {
        StringFormatValueIsValidRule.executeRule(
                (String) data[0],
                "correo electrónico",
                EMAIL_PATTERN,
                true
        );
    }
}
