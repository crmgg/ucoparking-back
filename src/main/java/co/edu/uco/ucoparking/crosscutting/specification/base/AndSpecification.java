package co.edu.uco.ucoparking.crosscutting.specification.base;

public final class AndSpecification <T> extends Specification<T> {

    Specification<T> left;
    Specification<T> right;

    public AndSpecification(Specification<T> left, Specification<T> right) {
        super();
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean isSatisfiedBy(T data) {
        return left.isSatisfiedBy(data) && right.isSatisfiedBy(data);}
}
