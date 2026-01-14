package mk.ukim.finki.emc.academic_assessment_system_backend.repository;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStaffAssignmentRepository extends JpaRepository<CourseStaffAssignment, Long> {

    List<CourseStaffAssignment> findByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);

    @Query("""
                SELECT csa FROM CourseStaffAssignment csa
                JOIN FETCH csa.user u
                WHERE csa.course.id = :courseId
            """)
    List<CourseStaffAssignment> findAllByCourseIdWithUser(@Param("courseId") Long courseId);
}
