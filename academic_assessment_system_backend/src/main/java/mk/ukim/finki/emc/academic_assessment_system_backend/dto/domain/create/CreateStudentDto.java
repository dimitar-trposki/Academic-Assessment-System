package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;

import java.util.List;

public record CreateStudentDto(

        @NotBlank(message = "Student index is required")
        @Size(max = 30, message = "Student index must be at most 30 characters")
        String studentIndex,

        @NotBlank(message = "Major is required")
        @Size(max = 120, message = "Major must be at most 120 characters")
        String major,

        @NotNull(message = "User ID is required")
        Long userId
) {

    public Student toStudent(User user) {
        return new Student(studentIndex, major, user);
    }

    public static CreateStudentDto from(Student student) {
        return new CreateStudentDto(
                student.getStudentIndex(),
                student.getMajor(),
                student.getUser().getId()
        );
    }

    public static List<CreateStudentDto> from(List<Student> students) {
        return students.stream()
                .map(CreateStudentDto::from)
                .toList();
    }
}
