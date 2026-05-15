package co.edu.uco.ucoparking.crosscutting.specification;

public final class NotSpecification <T> extends Specification<T> {
    Specification<T> condition;

    public NotSpecification(Specification<T> condition) {
        super();
        this.condition = condition;
    }


    @Override
    public boolean isSatisfiedBy(T data) {
        return !condition.isSatisfiedBy(data);
    }
}
