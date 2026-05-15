package co.edu.uco.ucoparking.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.UUID;

public class InstitutionEntity {

    private UUID id;
    private String name;

    public InstitutionEntity(UUID id, String name) {
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

