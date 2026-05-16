package co.edu.uco.ucoparking.application.usecase.specification.generics;

import co.edu.uco.ucoparking.crosscutting.helper.TextHelper;
import co.edu.uco.ucoparking.crosscutting.specification.base.Specification;

public class StringLengthSpecification extends Specification<String> {

    private final int min;
    private final int max;
    private final boolean mustApplyTrim;

    public StringLengthSpecification(int min, int max, boolean mustApplyTrim) {
        super();
        this.min = min;
        this.max = max;
        this.mustApplyTrim = mustApplyTrim;
    }

    @Override
    public boolean isSatisfiedBy(final String data) {
        return TextHelper.lengthIsValid(data, min, max, mustApplyTrim);
    }
}
