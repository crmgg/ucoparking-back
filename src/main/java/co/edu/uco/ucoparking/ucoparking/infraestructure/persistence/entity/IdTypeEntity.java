package co.edu.uco.ucoparking.ucoparking.infraestructure.persistence.entity;

import java.util.UUID;

public class IdTypeEntity {

    private UUID id;

        public IdTypeEntity(UUID id) {
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
