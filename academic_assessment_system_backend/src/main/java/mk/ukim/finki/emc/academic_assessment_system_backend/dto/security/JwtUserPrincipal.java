package mk.ukim.finki.emc.academic_assessment_system_backend.dto.security;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.AcademicRole;

public record JwtUserPrincipal(Long id, String email, AcademicRole role) {

}
