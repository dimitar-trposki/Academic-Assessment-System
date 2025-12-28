package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.AcademicRole;

public record RegisterUserRequestDto(
        String firstName,
        String lastName,
        String email,
        String password,
        AcademicRole academicRole
) {

    public User toUser() {
        return new User(firstName, lastName, email, password, academicRole);
    }

}
