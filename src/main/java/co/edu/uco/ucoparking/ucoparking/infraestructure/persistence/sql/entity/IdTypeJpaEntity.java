package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.sql.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Tipo Identificacion")
public class IdTypeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    public IdTypeJpaEntity(UUID id) {
        super();
        setId(id);
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
