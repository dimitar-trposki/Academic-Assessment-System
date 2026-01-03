package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StudentExamRegistrationApplicationService {

    List<DisplayStudentExamRegistrationDto> findAll();

    Optional<DisplayStudentExamRegistrationDto> findById(Long id);

    DisplayStudentExamRegistrationDto save(CreateStudentExamRegistrationDto createStudentExamRegistrationDto);

    Optional<DisplayStudentExamRegistrationDto> update(Long id, CreateStudentExamRegistrationDto createStudentExamRegistrationDto);

    Optional<DisplayStudentExamRegistrationDto> deleteById(Long id);

    DisplayStudentExamRegistrationDto registerCurrentStudent(String email, Long examId);

    List<DisplayStudentExamRegistrationDto> findStudentExamRegistrationByStudentId(Long studentId);

    List<DisplayStudentExamRegistrationDto> findAllByExamId(Long examId);

    List<DisplayStudentExamRegistrationDto> findAllByExamIdAndExamStatus(Long examId, ExamStatus status);

    byte[] exportStudentsByExamStatusCsv(Long courseId, ExamStatus examStatus);

    int importStudentsCsv(Long courseId, MultipartFile file);

}
