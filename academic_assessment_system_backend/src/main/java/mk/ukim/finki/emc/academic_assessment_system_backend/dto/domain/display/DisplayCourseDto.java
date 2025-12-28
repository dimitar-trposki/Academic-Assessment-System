package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;

import java.util.List;

public record DisplayCourseDto(
        Long id,
        String courseCode,
        String courseName,
        Integer semester,
        Integer academicYear
) {

    public static DisplayCourseDto from(Course course) {
        return new DisplayCourseDto(
                course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                course.getSemester(),
                course.getAcademicYear()
        );
    }

    public static List<DisplayCourseDto> from(List<Course> courses) {
        return courses
                .stream()
                .map(DisplayCourseDto::from)
                .toList();
    }

}
