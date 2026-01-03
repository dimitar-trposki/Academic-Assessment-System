package mk.ukim.finki.emc.academic_assessment_system_backend.repository;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStaffAssignmentRepository extends JpaRepository<CourseStaffAssignment, Long> {

    List<CourseStaffAssignment> findByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);

}
