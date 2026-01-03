package mk.ukim.finki.emc.academic_assessment_system_backend.repository;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamRegistrationRepository extends JpaRepository<StudentExamRegistration, Long> {

    boolean existsByStudentIdAndExamId(Long studentId, Long examId);

    List<StudentExamRegistration> findStudentExamRegistrationByStudentId(Long studentId);

    List<StudentExamRegistration> findAllByExamId(Long examId);

    Optional<StudentExamRegistration> findByStudentIdAndExamId(Long studentId, Long examId);

    List<StudentExamRegistration> findAllByExamIdAndExamStatus(Long examId, ExamStatus examStatus);

}
