package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;

import java.util.List;
import java.util.Optional;

public interface ExamService {

    List<Exam> findAll();

    Optional<Exam> findById(Long id);

    Exam save(Exam exam);

    Optional<Exam> update(Long id, Exam exam);

    Optional<Exam> deleteById(Long id);

    List<Exam> findAllByCourseId(Long courseId);
}
