package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateExamDto(

        @NotBlank(message = "Exam session is required")
        String session,

        @NotNull(message = "Exam date is required")
        @FutureOrPresent(message = "Exam date cannot be in the past")
        LocalDate dateOfExam,

        @NotNull(message = "Capacity is required")
        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacityOfStudents,

        @NotEmpty(message = "At least one laboratory must be reserved")
        List<String> reservedLaboratories,

        @NotNull(message = "Start time is required")
        LocalTime startTime,

        @NotNull(message = "End time is required")
        LocalTime endTime
) {

    public Exam toExam() {
        return new Exam(
                session,
                dateOfExam,
                capacityOfStudents,
                startTime,
                endTime,
                reservedLaboratories
        );
    }

    public static CreateExamDto from(Exam exam) {
        return new CreateExamDto(
                exam.getSession(),
                exam.getDateOfExam(),
                exam.getCapacityOfStudents(),
                exam.getReservedLaboratories(),
                exam.getStartTime(),
                exam.getEndTime()
        );
    }

    public static List<CreateExamDto> from(List<Exam> exams) {
        return exams.stream()
                .map(CreateExamDto::from)
                .toList();
    }
}
