package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.sql.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "Tipo Identificacion")
public class IdTypeJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    //Completar

}
