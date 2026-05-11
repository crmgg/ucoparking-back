package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.mapper;

public interface JPAMapper<E, J>{

    E toEntity(J jpa);

    J toJPA(E entity);
}
