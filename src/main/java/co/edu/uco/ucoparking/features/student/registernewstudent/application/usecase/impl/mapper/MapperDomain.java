package co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.impl.mapper;

public interface MapperDomain <D, E>{

    D toDomain(E entity);

     E toEntity(D domain);
}
