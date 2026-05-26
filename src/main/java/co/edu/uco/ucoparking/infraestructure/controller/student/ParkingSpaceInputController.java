package co.edu.uco.ucoparking.infraestructure.controller.student;

import co.edu.uco.ucoparking.application.usecase.AdminReleaseParkingSpaceUseCase;
import co.edu.uco.ucoparking.application.usecase.OccupyParkingSpaceUseCase;
import co.edu.uco.ucoparking.application.usecase.ReleaseParkingSpaceUseCase;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.ReserveParkingSpaceInputPort;
import co.edu.uco.ucoparking.features.parkingspace.reserveparkingspace.application.inputport.dto.ReserveParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import co.edu.uco.ucoparking.infraestructure.service.NotificationGatewayService;
import co.edu.uco.ucoparking.infraestructure.service.StudentNotificationEmailResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/students")
public class ParkingSpaceInputController {

    private static final Logger log = LoggerFactory.getLogger(ParkingSpaceInputController.class);

    private final OccupyParkingSpaceUseCase occupyParkingSpaceUseCase;
    private final ReleaseParkingSpaceUseCase releaseParkingSpaceUseCase;
    private final ReserveParkingSpaceInputPort reserveParkingSpaceInputPort;
    private final NotificationGatewayService notificationGatewayService;
    private final StudentNotificationEmailResolver studentNotificationEmailResolver;
    private final AdminReleaseParkingSpaceUseCase adminReleaseParkingSpaceUseCase;

    public ParkingSpaceInputController(OccupyParkingSpaceUseCase occupyParkingSpaceUseCase,
                                       ReleaseParkingSpaceUseCase releaseParkingSpaceUseCase,
                                       ReserveParkingSpaceInputPort reserveParkingSpaceInputPort,
                                       NotificationGatewayService notificationGatewayService,
                                       StudentNotificationEmailResolver studentNotificationEmailResolver,
                                       AdminReleaseParkingSpaceUseCase adminReleaseParkingSpaceUseCase) {
        this.occupyParkingSpaceUseCase = occupyParkingSpaceUseCase;
        this.releaseParkingSpaceUseCase = releaseParkingSpaceUseCase;
        this.reserveParkingSpaceInputPort = reserveParkingSpaceInputPort;
        this.notificationGatewayService = notificationGatewayService;
        this.studentNotificationEmailResolver = studentNotificationEmailResolver;
        this.adminReleaseParkingSpaceUseCase = adminReleaseParkingSpaceUseCase;
    }

    @PostMapping("/reserve")
    @CrossOrigin(origins = "*")
    public Mono<ParkingSpaceDTO> reserveParkingSpace(@RequestBody ReserveParkingSpaceDTO request) {
        return reserveParkingSpaceInputPort.execute(request)
                .flatMap(dto -> {
                    String recipient = studentNotificationEmailResolver.resolve(
                            request.getStudentEmail(), request.getStudentName());
                    if (recipient == null || recipient.isBlank()) {
                        log.warn("Reserva cupo {} sin correo resuelto (studentEmail={}, studentName={})",
                                request.getSpaceNumber(), request.getStudentEmail(), request.getStudentName());
                    } else {
                        log.info("Enviando correo de reserva cupo {} a {}", request.getSpaceNumber(), recipient);
                    }
                    return notificationGatewayService.sendReservationConfirmed(
                                    recipient,
                                    request.getStudentName(),
                                    request.getSpaceNumber(),
                                    request.getReservationStartTime(),
                                    request.getReservationEndTime(),
                                    request.getVehiclePlate())
                            .thenReturn(dto);
                });
    }

    @PostMapping("/admin/release/{spaceNumber}")
    @CrossOrigin(origins = "*")
    public Mono<ParkingSpaceDTO> adminReleaseParkingSpace(@PathVariable Integer spaceNumber) {
        log.info("Liberacion admin espacio {}", spaceNumber);
        return adminReleaseParkingSpaceUseCase.execute(spaceNumber);
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
