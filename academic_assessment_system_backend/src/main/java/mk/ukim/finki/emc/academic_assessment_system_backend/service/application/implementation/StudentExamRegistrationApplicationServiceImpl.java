package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentExamRegistrationApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.ExamService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentExamRegistrationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentExamRegistrationApplicationServiceImpl implements StudentExamRegistrationApplicationService {

    private final StudentExamRegistrationService studentExamRegistrationService;
    private final StudentService studentService;
    private final ExamService examService;

    public StudentExamRegistrationApplicationServiceImpl(StudentExamRegistrationService studentExamRegistrationService, StudentService studentService, ExamService examService) {
        this.studentExamRegistrationService = studentExamRegistrationService;
        this.studentService = studentService;
        this.examService = examService;
    }

    @Override
    public List<DisplayStudentExamRegistrationDto> findAll() {
        return DisplayStudentExamRegistrationDto
                .from(studentExamRegistrationService.findAll());
    }

    @Override
    public Optional<DisplayStudentExamRegistrationDto> findById(Long id) {
        return studentExamRegistrationService
                .findById(id)
                .map(DisplayStudentExamRegistrationDto::from);
    }

    @Override
    public DisplayStudentExamRegistrationDto save(CreateStudentExamRegistrationDto createStudentExamRegistrationDto) {
        Optional<Student> student = studentService.findById(createStudentExamRegistrationDto.studentId());
        Optional<Exam> exam = examService.findById(createStudentExamRegistrationDto.examId());

        return DisplayStudentExamRegistrationDto
                .from(studentExamRegistrationService.save(createStudentExamRegistrationDto.toStudentExamRegistration(student.get(), exam.get())));
    }

    @Override
    public Optional<DisplayStudentExamRegistrationDto> update(Long id, CreateStudentExamRegistrationDto createStudentExamRegistrationDto) {
        Optional<Student> student = studentService.findById(createStudentExamRegistrationDto.studentId());
        Optional<Exam> exam = examService.findById(createStudentExamRegistrationDto.examId());

        return studentExamRegistrationService
                .update(id, createStudentExamRegistrationDto.toStudentExamRegistration(student.get(), exam.get()))
                .map(DisplayStudentExamRegistrationDto::from);
    }

    @Override
    public Optional<DisplayStudentExamRegistrationDto> deleteById(Long id) {
        return studentExamRegistrationService
                .deleteById(id)
                .map(DisplayStudentExamRegistrationDto::from);
    }
}
