package co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "ProgramaAcademico")
public class AcademicProgramJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "institucion",  nullable = false)
    private InstitutionJpaEntity institution;

    @Column(name = "nombre", length =  50, nullable = false)
    private String name;

    protected AcademicProgramJpaEntity() {
    }

    public AcademicProgramJpaEntity(UUID id, InstitutionJpaEntity institution, String name) {
        super();
        setId(id);
        setInstitution(institution);
        setName(name);
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setInstitution(InstitutionJpaEntity institution) {
        this.institution = institution;
    }

    private void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public InstitutionJpaEntity getInstitution() {
        return institution;
    }

    public String getName() {
        return name;
    }
}
