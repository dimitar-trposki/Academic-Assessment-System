package mk.ukim.finki.emc.academic_assessment_system_backend.dto;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.AcademicRole;

public record CsvUserRowDto(
        String firstName,
        String lastName,
        String email,
        String password,
        AcademicRole academicRole,
        String studentIndex,
        String major
) {
}
