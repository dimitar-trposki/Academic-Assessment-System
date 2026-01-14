package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;

import java.util.List;

public record DisplayUserStudentDto(
        Long id,
        String studentIndex,
        String major,
        Long userId,
        String studentFirstName,
        String studentLastName,
        String studentEmail
) {
    public static DisplayUserStudentDto from(User user) {
        return new DisplayUserStudentDto(
                user.getStudent().getId(),
                user.getStudent().getStudentIndex(),
                user.getStudent().getMajor(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public static List<DisplayUserStudentDto> from(List<User> users) {
        return users.stream()
                .map(DisplayUserStudentDto::from)
                .toList();
    }
}
