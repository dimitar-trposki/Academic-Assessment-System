package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;

import java.util.List;

public record CreateStudentExamRegistrationDto(
        Long studentId,
        Long examId
) {

    public StudentExamRegistration toStudentExamRegistration(Student student, Exam exam) {
        return new StudentExamRegistration(student, exam);
    }

    public static CreateStudentExamRegistrationDto from(StudentExamRegistration registration) {
        return new CreateStudentExamRegistrationDto(
                registration.getStudent().getId(),
                registration.getExam().getId()
        );
    }

    public static List<CreateStudentExamRegistrationDto> from(
            List<StudentExamRegistration> registrations
    ) {
        return registrations.stream()
                .map(CreateStudentExamRegistrationDto::from)
                .toList();
    }
}
