package co.edu.uco.ucoparking.initializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"co.edu.uco.ucoparking"})
@SpringBootApplication
public class UcoParkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcoParkingApplication.class, args);
    }

}
