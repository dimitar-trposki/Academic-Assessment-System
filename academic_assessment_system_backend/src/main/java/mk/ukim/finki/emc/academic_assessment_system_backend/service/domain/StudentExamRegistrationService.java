package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;

import java.util.List;
import java.util.Optional;

public interface StudentExamRegistrationService {

    List<StudentExamRegistration> findAll();

    Optional<StudentExamRegistration> findById(Long id);

    StudentExamRegistration save(StudentExamRegistration studentExamRegistration);

    Optional<StudentExamRegistration> update(Long id, StudentExamRegistration studentExamRegistration);

    Optional<StudentExamRegistration> deleteById(Long id);

}
