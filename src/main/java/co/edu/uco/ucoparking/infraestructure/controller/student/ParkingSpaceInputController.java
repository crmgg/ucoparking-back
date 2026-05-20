package co.edu.uco.ucoparking.infraestructure.controller.student;

import co.edu.uco.ucoparking.application.usecase.OccupyParkingSpaceUseCase;
import co.edu.uco.ucoparking.infraestructure.controller.dto.ParkingSpaceDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/uco-parking/v1/parking-spaces/commands")
public class ParkingSpaceInputController {

    private final OccupyParkingSpaceUseCase occupyParkingSpaceUseCase;

    public ParkingSpaceInputController(OccupyParkingSpaceUseCase occupyParkingSpaceUseCase) {
        this.occupyParkingSpaceUseCase = occupyParkingSpaceUseCase;
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
