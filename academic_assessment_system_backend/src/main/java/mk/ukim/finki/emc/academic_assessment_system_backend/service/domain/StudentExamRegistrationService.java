package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StudentExamRegistrationService {

    List<StudentExamRegistration> findAll();

    Optional<StudentExamRegistration> findById(Long id);

    StudentExamRegistration save(StudentExamRegistration studentExamRegistration);

    Optional<StudentExamRegistration> update(Long id, StudentExamRegistration studentExamRegistration);

    Optional<StudentExamRegistration> deleteById(Long id);

    StudentExamRegistration register(Long studentId, Long examId);

    List<StudentExamRegistration> findStudentExamRegistrationByStudentId(Long studentId);

    List<StudentExamRegistration> findAllByExamId(Long examId);

    List<StudentExamRegistration> findAllByExamIdAndExamStatus(Long examId, ExamStatus status);

}
