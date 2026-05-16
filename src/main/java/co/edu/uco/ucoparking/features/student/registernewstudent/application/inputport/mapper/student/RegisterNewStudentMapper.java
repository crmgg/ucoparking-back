package co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.mapper.student;

import co.edu.uco.ucoparking.application.inputport.mapper.DTOMapper;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.inputport.dto.RegisterNewStudentDTO;
import co.edu.uco.ucoparking.features.student.registernewstudent.application.usecase.domain.RegisterNewStudentDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterNewStudentMapper extends DTOMapper<RegisterNewStudentDTO, RegisterNewStudentDomain> {

}
