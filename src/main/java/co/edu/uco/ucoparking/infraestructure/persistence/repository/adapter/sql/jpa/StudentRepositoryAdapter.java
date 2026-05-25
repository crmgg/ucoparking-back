package co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa;

import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.entity.StudentEntity;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.adapter.sql.jpa.mapper.StudentJpaMapper;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.StudentRepository;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.AcademicProgramJPARepository;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.IdTypeJPARepository;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.StudentJPARepository;
import co.edu.uco.ucoparking.infraestructure.persistence.repository.sql.entity.StudentJpaEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public class StudentRepositoryAdapter implements StudentRepository {

    private final StudentJPARepository repository;
    private final AcademicProgramJPARepository academicProgramRepository;
    private final IdTypeJPARepository idTypeRepository;
    private final StudentJpaMapper mapper;

    public StudentRepositoryAdapter(
            StudentJPARepository repository,
            AcademicProgramJPARepository academicProgramRepository,
            IdTypeJPARepository idTypeRepository,
            StudentJpaMapper mapper) {
        this.repository = repository;
        this.academicProgramRepository = academicProgramRepository;
        this.idTypeRepository = idTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> create(StudentEntity entity) {
        return Mono.fromRunnable(() -> {
            var programRef = academicProgramRepository.getReferenceById(entity.getAcademicProgram().getId());
            var idTypeRef = idTypeRepository.getReferenceById(entity.getIdType().getId());
            StudentJpaEntity jpaEntity = mapper.toJpaEntity(entity, programRef, idTypeRef);
            repository.save(jpaEntity);
        });
    }

    @Override
    public StudentEntity findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toEntity)
                .orElse(null);
    }

    @Override
    public List<StudentEntity> findByFilter(StudentEntity entity) {
        return repository.findAll().stream()
                .filter(jpa -> matchesFilter(jpa, entity))
                .map(mapper::toEntity)
                .toList();
    }

    @Override
    public List<StudentEntity> findAll(StudentEntity entity) {
        return repository.findAll().stream()
                .map(mapper::toEntity)
                .toList();
    }

    private boolean matchesFilter(StudentJpaEntity jpa, StudentEntity filter) {
        if (filter == null) {
            return true;
        }
        if (filter.getEmail() != null && !filter.getEmail().equalsIgnoreCase(jpa.getEmail())) {
            return false;
        }
        if (filter.getId() != null && !filter.getId().equals(jpa.getId())) {
            return false;
        }
        return true;
    }
}
