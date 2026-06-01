package co.edu.uco.ucoparking.initializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.r2dbc.autoconfigure.R2dbcTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"co.edu.uco.ucoparking"})
@EnableScheduling
@SpringBootApplication(exclude = R2dbcTransactionManagerAutoConfiguration.class)
public class UcoParkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcoParkingApplication.class, args);
    }

}
