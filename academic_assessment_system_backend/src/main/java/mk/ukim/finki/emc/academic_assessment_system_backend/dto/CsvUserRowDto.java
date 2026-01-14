package mk.ukim.finki.emc.academic_assessment_system_backend.dto;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;

public record CsvUserRowDto(
        String firstName,
        String lastName,
        String email,
        String password,
        UserRole userRole,
        String studentIndex,
        String major
) {
}
