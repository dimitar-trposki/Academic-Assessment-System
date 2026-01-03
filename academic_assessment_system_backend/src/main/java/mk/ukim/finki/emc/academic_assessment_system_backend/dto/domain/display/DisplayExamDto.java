package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DisplayExamDto(
        Long id,
        String session,
        LocalDate dateOfExam,
        Integer capacityOfStudents,
        Course course,
        List<String> reservedLaboratories,
        LocalTime startTime,
        LocalTime endTime
) {

    public static DisplayExamDto from(Exam exam) {
        return new DisplayExamDto(
                exam.getId(),
                exam.getSession(),
                exam.getDateOfExam(),
                exam.getCapacityOfStudents(),
                exam.getCourse(),
                exam.getReservedLaboratories(),
                exam.getStartTime(),
                exam.getEndTime()
        );
    }

    public static List<DisplayExamDto> from(List<Exam> exams) {
        return exams.stream()
                .map(DisplayExamDto::from)
                .toList();
    }
}
