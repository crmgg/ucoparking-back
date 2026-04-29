package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.sql.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Programa Academico")
public class AcademicProgramJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "institucion")
    private InstitutionJpaEntity institution;

    @Column(name = "nombre")
    private String name;

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
