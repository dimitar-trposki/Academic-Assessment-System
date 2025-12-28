package mk.ukim.finki.emc.academic_assessment_system_backend.repository;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
