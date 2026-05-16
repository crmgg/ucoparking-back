package co.edu.uco.ucoparking.crosscutting.specification.base;

public abstract class Specification <T> {

    public abstract boolean isSatisfiedBy(T data);

    public Specification<T> and(Specification<T> other) {
        return new AndSpecification<T>(this, other);
    }

    public Specification<T> or(Specification<T> other) {
        return new OrSpecification<T>(this, other);
    }

    public Specification<T> not() {
        return new NotSpecification<T>(this);
    }

}