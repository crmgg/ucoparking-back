package co.edu.uco.ucoparking.infraestructure.controller.student;

import co.edu.uco.ucoparking.application.usecase.OccupyParkingSpaceUseCase;
import co.edu.uco.ucoparking.application.usecase.ReleaseParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.ReserveParkingSpaceInputPort;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.service.NotificationGatewayService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/students")
public class ParkingSpaceInputController {

    private final OccupyParkingSpaceUseCase occupyParkingSpaceUseCase;
    private final ReleaseParkingSpaceUseCase releaseParkingSpaceUseCase;
    private final ReserveParkingSpaceInputPort reserveParkingSpaceInputPort;
    private final NotificationGatewayService notificationGatewayService;

    public ParkingSpaceInputController(OccupyParkingSpaceUseCase occupyParkingSpaceUseCase,
                                       ReleaseParkingSpaceUseCase releaseParkingSpaceUseCase,
                                       ReserveParkingSpaceInputPort reserveParkingSpaceInputPort,
                                       NotificationGatewayService notificationGatewayService) {
        this.occupyParkingSpaceUseCase = occupyParkingSpaceUseCase;
        this.releaseParkingSpaceUseCase = releaseParkingSpaceUseCase;
        this.reserveParkingSpaceInputPort = reserveParkingSpaceInputPort;
        this.notificationGatewayService = notificationGatewayService;
    }

    @PostMapping("/reserve")
    @CrossOrigin(origins = "*")
    public Mono<ParkingSpaceDTO> reserveParkingSpace(@RequestBody ReserveParkingSpaceDTO request) {
        return reserveParkingSpaceInputPort.execute(request)
                .flatMap(dto -> notificationGatewayService.sendReservationConfirmed(
                                request.getStudentEmail(),
                                request.getStudentName(),
                                request.getSpaceNumber())
                        .thenReturn(dto));
    }

    @PostMapping("/release")
    @CrossOrigin(origins = "*")
    public Mono<ParkingSpaceDTO> releaseParkingSpace(@RequestBody ReleaseParkingSpaceRequest request) {
        return releaseParkingSpaceUseCase.execute(
                request.getSpaceNumber(),
                request.getStudentId()
        );
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

    public static class ReleaseParkingSpaceRequest {
        private Integer spaceNumber;
        private String studentId;

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
