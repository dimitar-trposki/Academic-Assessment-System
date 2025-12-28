package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.AcademicRole;

import java.util.List;

public record DisplayUserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        AcademicRole academicRole
) {

    public static DisplayUserDto from(User user) {
        return new DisplayUserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAcademicRole()
        );
    }

    public static List<DisplayUserDto> from(List<User> users) {
        return users.stream()
                .map(DisplayUserDto::from)
                .toList();
    }
}
