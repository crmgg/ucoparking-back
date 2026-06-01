package co.edu.uco.ucoparking;

import co.edu.uco.ucoparking.initializer.UcoParkingApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = UcoParkingApplication.class)
@ActiveProfiles("test")
class UcoParkingApplicationTests {

    @Test
    void contextLoads() {
    }

}
