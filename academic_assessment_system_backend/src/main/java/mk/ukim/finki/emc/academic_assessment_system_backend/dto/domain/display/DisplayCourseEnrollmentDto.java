package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;

import java.util.List;

public record DisplayCourseEnrollmentDto(
        Long id,
        Long studentId,
        Long courseId
) {

    public static DisplayCourseEnrollmentDto from(CourseEnrollment enrollment) {
        return new DisplayCourseEnrollmentDto(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId()
        );
    }

    public static List<DisplayCourseEnrollmentDto> from(
            List<CourseEnrollment> enrollments
    ) {
        return enrollments.stream()
                .map(DisplayCourseEnrollmentDto::from)
                .toList();
    }
}
