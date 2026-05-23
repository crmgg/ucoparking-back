package co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Institucion")
public class InstitutionJpaEntity {

    protected InstitutionJpaEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nombre", length = 50, nullable = false)
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
