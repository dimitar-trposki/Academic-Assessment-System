package mk.ukim.finki.emc.academic_assessment_system_backend.repository;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
}
