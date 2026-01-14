package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;

import java.util.List;

public record DisplayStudentDto(
        Long id,
        String studentIndex,
        String major,
        Long userId,
        String studentFirstName,
        String studentLastName,
        String studentEmail
) {

    public static DisplayStudentDto from(Student student) {
        return new DisplayStudentDto(
                student.getId(),
                student.getStudentIndex(),
                student.getMajor(),
                student.getUser().getId(),
                student.getUser().getFirstName(),
                student.getUser().getLastName(),
                student.getUser().getEmail()
        );
    }

    public static List<DisplayStudentDto> from(List<Student> students) {
        return students.stream()
                .map(DisplayStudentDto::from)
                .toList();
    }
}
