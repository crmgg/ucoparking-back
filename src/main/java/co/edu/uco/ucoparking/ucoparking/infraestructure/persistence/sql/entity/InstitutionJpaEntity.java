package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "Institucion")
public class InstitutionJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Id
    @Column(name = "nombre")
    private String name;

    public InstitutionJpaEntity(UUID id, String name) {
        super();
        setId(id);
        setName(name);
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
