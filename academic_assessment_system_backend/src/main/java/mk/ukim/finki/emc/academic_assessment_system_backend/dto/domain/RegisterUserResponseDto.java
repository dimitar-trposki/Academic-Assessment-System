package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;

public record RegisterUserResponseDto(
        String firstName,
        String lastName,
        String email,
        String password,
        UserRole userRole
) {

    public static RegisterUserResponseDto from(User user) {
        return new RegisterUserResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserRole()
        );
    }

}