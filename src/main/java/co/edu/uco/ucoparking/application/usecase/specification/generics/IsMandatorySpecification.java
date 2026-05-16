package co.edu.uco.ucoparking.application.usecase.specification.generics;

import co.edu.uco.ucoparking.crosscutting.helper.ObjectHelper;
import co.edu.uco.ucoparking.crosscutting.specification.base.Specification;

public class IsMandatorySpecification extends Specification<Object> {

    @Override
    public boolean isSatisfiedBy(Object data) {
        return !ObjectHelper.isNull(data);
    }
}
