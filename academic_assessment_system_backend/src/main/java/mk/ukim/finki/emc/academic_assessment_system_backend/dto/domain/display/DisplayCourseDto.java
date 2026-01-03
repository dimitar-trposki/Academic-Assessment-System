package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.StaffRole;

import java.util.Collections;
import java.util.List;

public record DisplayCourseDto(
        Long id,
        String courseCode,
        String courseName,
        Integer semester,
        Integer academicYear,
        List<DisplayUserDto> professors,
        List<DisplayUserDto> assistants
) {
    public static DisplayCourseDto from(Course course) {
        List<CourseStaffAssignment> assignments =
                course.getCourseStaffAssignments() == null ? Collections.emptyList() : course.getCourseStaffAssignments();

        List<DisplayUserDto> professors = assignments.stream()
                .filter(a -> a.getStaffRole() == StaffRole.PROFESSOR)
                .map(a -> DisplayUserDto.from(a.getUser()))
                .distinct()
                .toList();

        List<DisplayUserDto> assistants = assignments.stream()
                .filter(a -> a.getStaffRole() == StaffRole.ASSISTANT)
                .map(a -> DisplayUserDto.from(a.getUser()))
                .distinct()
                .toList();

        return new DisplayCourseDto(
                course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                course.getSemester(),
                course.getAcademicYear(),
                professors,
                assistants
        );
    }

    public static List<DisplayCourseDto> from(List<Course> courses) {
        return courses.stream()
                .map(DisplayCourseDto::from)
                .toList();
    }
}
