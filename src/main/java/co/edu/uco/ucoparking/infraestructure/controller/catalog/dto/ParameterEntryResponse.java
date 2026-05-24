package co.edu.uco.ucoparking.infraestructure.controller.catalog.dto;

import java.io.Serializable;

public class ParameterEntryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String value;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
