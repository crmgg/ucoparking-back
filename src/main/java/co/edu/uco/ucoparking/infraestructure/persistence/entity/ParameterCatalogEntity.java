package co.edu.uco.ucoparking.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parameter_catalog")
public class ParameterCatalogEntity {

    @Id
    @Column(name = "param_key", length = 64)
    private String paramKey;

    @Column(name = "param_value", length = 256, nullable = false)
    private String paramValue;

    @Column(name = "description", length = 512)
    private String description;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
