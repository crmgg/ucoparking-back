package co.edu.uco.ucoparking.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ucoParkingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UCO Parking API")
                        .description("API REST reactiva para reserva de parqueaderos UCO")
                        .version("1.0.0"));
    }
}
