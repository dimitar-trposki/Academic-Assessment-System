package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;

import java.util.List;
import java.util.Optional;

public interface CourseStaffAssignmentApplicationService {

    List<DisplayCourseStaffAssignmentDto> findAll();

    Optional<DisplayCourseStaffAssignmentDto> findById(Long id);

    DisplayCourseStaffAssignmentDto save(CreateCourseStaffAssignmentDto createCourseStaffAssignmentDto);

    Optional<DisplayCourseStaffAssignmentDto> update(Long id, CreateCourseStaffAssignmentDto createCourseStaffAssignmentDto);

    Optional<DisplayCourseStaffAssignmentDto> deleteById(Long id);

    List<CourseStaffAssignment> findByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);

    List<DisplayCourseStaffAssignmentDto> findAllByCourseIdWithUser(Long courseId);

    List<DisplayCourseStaffAssignmentDto> findCourseStaffAssignmentByUserId(Long userId);

}
