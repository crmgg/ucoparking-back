package co.edu.uco.ucoparking.infraestructure.controller.student;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import co.edu.uco.ucoparking.application.outputport.ParkingSpaceOutputPort;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;

@RestController
@RequestMapping("/uco-parking/v1/students")
public class ParkingSpaceOutputController {

    private final ParkingSpaceOutputPort parkingSpaceOutputPort;

    public ParkingSpaceOutputController(ParkingSpaceOutputPort parkingSpaceOutputPort) {
        this.parkingSpaceOutputPort = parkingSpaceOutputPort;
    }

    @GetMapping
    @CrossOrigin(origins = "*")
    public Flux<ParkingSpaceDTO> getAllParkingSpaces() {
        return parkingSpaceOutputPort.getAllParkingSpaces();
    }

    @GetMapping("/number/{spaceNumber}")
    @CrossOrigin(origins = "*")
    public Mono<ParkingSpaceDTO> getParkingSpaceByNumber(@PathVariable Integer spaceNumber) {
        return parkingSpaceOutputPort.getParkingSpaceByNumber(spaceNumber);
    }

    @GetMapping("/subscribe/{spaceNumber}")
    @CrossOrigin(origins = "*")
    public Flux<ParkingSpaceDTO> subscribeToSpaceUpdates(@PathVariable Integer spaceNumber) {
        return parkingSpaceOutputPort.subscribeToParkingSpaceUpdates(spaceNumber);
    }

    @GetMapping("/status/occupied")
    @CrossOrigin(origins = "*")
    public Flux<ParkingSpaceDTO> getOccupiedSpaces() {
        return parkingSpaceOutputPort.getOccupiedParkingSpaces();
    }

    @GetMapping("/status/available")
    @CrossOrigin(origins = "*")
    public Flux<ParkingSpaceDTO> getAvailableSpaces() {
        return parkingSpaceOutputPort.getAvailableParkingSpaces();
    }
}
