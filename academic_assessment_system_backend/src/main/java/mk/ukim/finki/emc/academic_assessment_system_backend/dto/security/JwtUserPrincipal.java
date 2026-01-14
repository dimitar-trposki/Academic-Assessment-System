package mk.ukim.finki.emc.academic_assessment_system_backend.dto.security;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;

public record JwtUserPrincipal(Long id, String email, UserRole role) {

}
