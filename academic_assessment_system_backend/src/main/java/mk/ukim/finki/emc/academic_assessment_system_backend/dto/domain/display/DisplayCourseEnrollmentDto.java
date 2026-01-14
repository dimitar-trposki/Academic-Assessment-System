package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;

import java.util.List;

public record DisplayCourseEnrollmentDto(
        Long id,
        Long studentId,
        Long courseId,
        String courseName,
        String courseCode,
        Integer courseSemester,
        String studentFirstName,
        String studentLastName,
        String studentEmail,
        String studentIndex,
        String studentMajor
) {

    public static DisplayCourseEnrollmentDto from(CourseEnrollment enrollment) {
        return new DisplayCourseEnrollmentDto(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getCourseName(),
                enrollment.getCourse().getCourseCode(),
                enrollment.getCourse().getSemester(),
                enrollment.getStudent().getUser().getFirstName(),
                enrollment.getStudent().getUser().getLastName(),
                enrollment.getStudent().getUser().getEmail(),
                enrollment.getStudent().getStudentIndex(),
                enrollment.getStudent().getMajor()
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
