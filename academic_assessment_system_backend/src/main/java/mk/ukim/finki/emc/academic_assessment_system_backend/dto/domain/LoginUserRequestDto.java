package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain;

public record LoginUserRequestDto(
        String email,
        String password
) {
}
