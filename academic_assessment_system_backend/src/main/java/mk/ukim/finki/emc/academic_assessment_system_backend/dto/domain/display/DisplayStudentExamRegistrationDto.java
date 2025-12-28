package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;

import java.util.List;

public record DisplayStudentExamRegistrationDto(
        Long id,
        Long studentId,
        Long examId,
        ExamStatus examStatus
) {

    public static DisplayStudentExamRegistrationDto from(
            StudentExamRegistration registration
    ) {
        return new DisplayStudentExamRegistrationDto(
                registration.getId(),
                registration.getStudent().getId(),
                registration.getExam().getId(),
                registration.getExamStatus()
        );
    }

    public static List<DisplayStudentExamRegistrationDto> from(
            List<StudentExamRegistration> registrations
    ) {
        return registrations.stream()
                .map(DisplayStudentExamRegistrationDto::from)
                .toList();
    }
}
