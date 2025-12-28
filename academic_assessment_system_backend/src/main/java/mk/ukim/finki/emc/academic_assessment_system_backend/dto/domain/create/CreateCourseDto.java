package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;

import java.util.List;

public record CreateCourseDto(

        @NotBlank(message = "Course code is required")
        @Size(max = 30, message = "Course code must be at most 30 characters")
        String courseCode,

        @NotBlank(message = "Course name is required")
        @Size(max = 200, message = "Course name must be at most 200 characters")
        String courseName,

        @NotNull(message = "Semester is required")
        @Min(value = 1, message = "Semester must be at least 1")
        @Max(value = 12, message = "Semester must be at most 12")
        Integer semester,

        @NotNull(message = "Academic year is required")
        @Min(value = 2000, message = "Academic year must be >= 2000")
        @Max(value = 2100, message = "Academic year must be <= 2100")
        Integer academicYear
) {

    public static CreateCourseDto from(Course course) {
        return new CreateCourseDto(
                course.getCourseCode(),
                course.getCourseName(),
                course.getSemester(),
                course.getAcademicYear()
        );
    }

    public Course toCourse() {
        return new Course(courseCode, courseName, semester, academicYear);
    }

    public static List<CreateCourseDto> from(List<Course> courses) {
        return courses.stream()
                .map(CreateCourseDto::from)
                .toList();
    }
}
