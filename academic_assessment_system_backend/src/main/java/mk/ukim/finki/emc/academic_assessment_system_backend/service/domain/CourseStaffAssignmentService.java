package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;

import java.util.List;
import java.util.Optional;

public interface CourseStaffAssignmentService {

    List<CourseStaffAssignment> findAll();

    Optional<CourseStaffAssignment> findById(Long id);

    CourseStaffAssignment save(CourseStaffAssignment courseStaffAssignment);

    Optional<CourseStaffAssignment> update(Long id, CourseStaffAssignment courseStaffAssignment);

    Optional<CourseStaffAssignment> deleteById(Long id);

    List<CourseStaffAssignment> findByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);

    List<CourseStaffAssignment> findAllByCourseIdWithUser(Long courseId);

}
