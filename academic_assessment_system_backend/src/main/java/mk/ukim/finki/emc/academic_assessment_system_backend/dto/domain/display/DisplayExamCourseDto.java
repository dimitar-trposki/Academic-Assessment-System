package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;

public record DisplayExamCourseDto(
        Long id,
        String courseCode,
        String courseName
) {

    public static DisplayExamCourseDto from(Course course) {
        if (course == null) {
            return null;
        }

        return new DisplayExamCourseDto(
                course.getId(),
                course.getCourseCode(),
                course.getCourseName()
        );
    }
}