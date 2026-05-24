package co.edu.uco.ucoparking.application.usecase.specification.generics;

import co.edu.uco.ucoparking.crosscutting.helper.TextHelper;
import co.edu.uco.ucoparking.crosscutting.specification.base.Specification;

public class IsStringValuePresentSpecification extends Specification<String> {

    private final boolean mustApplyTrim;

    public IsStringValuePresentSpecification(boolean mustApplyTrim) {
        super();
        this.mustApplyTrim = mustApplyTrim;
    }

    @Override
    public boolean isSatisfiedBy(String data) {
        return mustApplyTrim
                ? !TextHelper.isEmptyWithTrim(data)
                : !TextHelper.isEmpty(data);
    }
}
