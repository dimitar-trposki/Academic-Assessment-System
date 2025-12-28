package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.StaffRole;

import java.util.List;

public record CreateCourseStaffAssignmentDto(
        Long userId,
        Long courseId,
        StaffRole staffRole
) {

    public CourseStaffAssignment toCourseStaffAssignment(User user, Course course) {
        return new CourseStaffAssignment(user, course, staffRole);
    }

    public static CreateCourseStaffAssignmentDto from(CourseStaffAssignment assignment) {
        return new CreateCourseStaffAssignmentDto(
                assignment.getUser().getId(),
                assignment.getCourse().getId(),
                assignment.getStaffRole()
        );
    }

    public static List<CreateCourseStaffAssignmentDto> from(List<CourseStaffAssignment> assignments) {
        return assignments.stream()
                .map(CreateCourseStaffAssignmentDto::from)
                .toList();
    }
}
