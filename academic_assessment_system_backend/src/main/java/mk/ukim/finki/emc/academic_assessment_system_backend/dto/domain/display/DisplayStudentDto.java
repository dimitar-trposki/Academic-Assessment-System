package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;

import java.util.List;

public record DisplayStudentDto(
        Long id,
        String studentIndex,
        String major,
        Long userId
) {

    public static DisplayStudentDto from(Student student) {
        return new DisplayStudentDto(
                student.getId(),
                student.getStudentIndex(),
                student.getMajor(),
                student.getUser().getId()
        );
    }

    public static List<DisplayStudentDto> from(List<Student> students) {
        return students.stream()
                .map(DisplayStudentDto::from)
                .toList();
    }
}
