package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

public interface PasswordResetApplicationService {

    String requestReset(String email);

    void confirmReset(String token, String newPassword);

}
