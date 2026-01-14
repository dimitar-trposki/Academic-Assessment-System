package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;

import java.util.List;

public record DisplayMyProfileDto(
        Long userId,
        String firstName,
        String lastName,
        String email,
        UserRole userRole,

        Long studentId,
        String studentIndex,
        String major,

        Long staffId,
        String staffRole,

        List<DisplayCourseEnrollmentDto> asStudent,
        List<DisplayCourseStaffAssignmentDto> asStaff
) {
}
