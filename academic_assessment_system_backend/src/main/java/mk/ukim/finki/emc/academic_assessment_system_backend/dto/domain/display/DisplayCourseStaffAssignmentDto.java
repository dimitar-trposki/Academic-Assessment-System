package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.StaffRole;

import java.util.List;

public record DisplayCourseStaffAssignmentDto(
        Long id,
        Long userId,
        Long courseId,
        StaffRole staffRole
) {

    public static DisplayCourseStaffAssignmentDto from(CourseStaffAssignment assignment) {
        return new DisplayCourseStaffAssignmentDto(
                assignment.getId(),
                assignment.getUser().getId(),
                assignment.getCourse().getId(),
                assignment.getStaffRole()
        );
    }

    public static List<DisplayCourseStaffAssignmentDto> from(
            List<CourseStaffAssignment> assignments
    ) {
        return assignments.stream()
                .map(DisplayCourseStaffAssignmentDto::from)
                .toList();
    }
}
