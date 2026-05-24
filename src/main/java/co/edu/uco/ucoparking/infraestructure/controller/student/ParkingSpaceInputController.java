package co.edu.uco.ucoparking.infraestructure.controller.student;

import co.edu.uco.ucoparking.application.outputport.ParkingSpaceOutputPort;
import co.edu.uco.ucoparking.application.usecase.OccupyParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.ReserveParkingSpaceInputPort;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/students")
public class ParkingSpaceInputController {

    private final OccupyParkingSpaceUseCase occupyParkingSpaceUseCase;
    private final ReserveParkingSpaceInputPort reserveParkingSpaceInputPort;
    private final ParkingSpaceOutputPort parkingSpaceOutputPort;

    public ParkingSpaceInputController(OccupyParkingSpaceUseCase occupyParkingSpaceUseCase,
                                       ReserveParkingSpaceInputPort reserveParkingSpaceInputPort,
                                       ParkingSpaceOutputPort parkingSpaceOutputPort) {
        this.occupyParkingSpaceUseCase = occupyParkingSpaceUseCase;
        this.reserveParkingSpaceInputPort = reserveParkingSpaceInputPort;
        this.parkingSpaceOutputPort = parkingSpaceOutputPort;
    }

    @PostMapping("/reserve")
    @CrossOrigin(origins = "*")
    public Mono<ParkingSpaceDTO> reserveParkingSpace(@RequestBody ReserveParkingSpaceDTO request) {
        return reserveParkingSpaceInputPort.execute(request)
                .then(parkingSpaceOutputPort.getParkingSpaceByNumber(request.getSpaceNumber()));
    }

    @PostMapping("/occupy")
    @CrossOrigin(origins = "*")
    public Mono<ParkingSpaceDTO> occupyParkingSpace(@RequestBody OccupyParkingSpaceRequest request) {
        return occupyParkingSpaceUseCase.execute(
                request.getSpaceNumber(),
                request.getStudentId(),
                request.getStudentName()
        );
    }

    public static class OccupyParkingSpaceRequest {
        private Integer spaceNumber;
        private String studentId;
        private String studentName;

        public Integer getSpaceNumber() {
            return spaceNumber;
        }

        public void setSpaceNumber(Integer spaceNumber) {
            this.spaceNumber = spaceNumber;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }
    }
}
