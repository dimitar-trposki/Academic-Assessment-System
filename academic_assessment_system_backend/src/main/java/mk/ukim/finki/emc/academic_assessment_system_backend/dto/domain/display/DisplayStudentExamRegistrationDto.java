package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DisplayStudentExamRegistrationDto(
        Long id,
        Long studentId,
        Long examId,
        ExamStatus examStatus,
        String studentIndex,
        String examCourse,
        String examSession,
        LocalDate examDate,
        LocalTime startTime
) {

    public static DisplayStudentExamRegistrationDto from(
            StudentExamRegistration registration
    ) {
        return new DisplayStudentExamRegistrationDto(
                registration.getId(),
                registration.getStudent().getId(),
                registration.getExam().getId(),
                registration.getExamStatus(),
                registration.getStudent().getStudentIndex(),
                registration.getExam().getCourse().getCourseCode(),
                registration.getExam().getSession(),
                registration.getExam().getDateOfExam(),
                registration.getExam().getStartTime()
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
