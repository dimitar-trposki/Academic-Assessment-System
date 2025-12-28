package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;

import java.util.List;

public record CreateCourseEnrollmentDto(
        Long studentId,
        Long courseId
) {

    public CourseEnrollment toCourseEnrollment(Student student, Course course) {
        return new CourseEnrollment(null, student, course);
    }

    public static CreateCourseEnrollmentDto from(CourseEnrollment enrollment) {
        return new CreateCourseEnrollmentDto(
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId()
        );
    }

    public static List<CreateCourseEnrollmentDto> from(List<CourseEnrollment> enrollments) {
        return enrollments.stream().map(CreateCourseEnrollmentDto::from).toList();
    }

}