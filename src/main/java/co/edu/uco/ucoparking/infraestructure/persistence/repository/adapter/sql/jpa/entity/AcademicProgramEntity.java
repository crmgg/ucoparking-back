package co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.InstitutionJpaEntity;
import jakarta.persistence.Column;

import java.util.UUID;

public class AcademicProgramEntity {

    private UUID id;
    private InstitutionJpaEntity institution;

    @Column(name = "nombre")
    private String name;

    public AcademicProgramEntity(UUID id, InstitutionJpaEntity institution, String name) {
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
