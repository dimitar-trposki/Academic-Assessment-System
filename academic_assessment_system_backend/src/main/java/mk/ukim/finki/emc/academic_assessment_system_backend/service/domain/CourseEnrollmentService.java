package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;

import java.util.List;
import java.util.Optional;

public interface CourseEnrollmentService {

    List<CourseEnrollment> findAll();

    Optional<CourseEnrollment> findById(Long id);

    CourseEnrollment save(CourseEnrollment courseEnrollment);

    Optional<CourseEnrollment> update(Long id, CourseEnrollment courseEnrollment);

    Optional<CourseEnrollment> deleteById(Long id);

}
