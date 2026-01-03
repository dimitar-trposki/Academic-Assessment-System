package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.ExamRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.StudentExamRegistrationRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.ExamService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentExamRegistrationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentExamRegistrationServiceImpl implements StudentExamRegistrationService {

    private final StudentExamRegistrationRepository studentExamRegistrationRepository;
    private final StudentService studentService;
    private final ExamService examService;

    public StudentExamRegistrationServiceImpl(StudentExamRegistrationRepository studentExamRegistrationRepository, StudentService studentService, ExamService examService) {
        this.studentExamRegistrationRepository = studentExamRegistrationRepository;
        this.studentService = studentService;
        this.examService = examService;
    }

    @Override
    public List<StudentExamRegistration> findAll() {
        return studentExamRegistrationRepository.findAll();
    }

    @Override
    public Optional<StudentExamRegistration> findById(Long id) {
        return studentExamRegistrationRepository.findById(id);
    }

    @Override
    public StudentExamRegistration save(StudentExamRegistration studentExamRegistration) {
        return studentExamRegistrationRepository.save(studentExamRegistration);
    }

    @Override
    public Optional<StudentExamRegistration> update(Long id, StudentExamRegistration studentExamRegistration) {
        return findById(id)
                .map(existingStudentExamRegistration -> {
                    existingStudentExamRegistration.setStudent(studentExamRegistration.getStudent());
                    existingStudentExamRegistration.setExam(studentExamRegistration.getExam());
                    existingStudentExamRegistration.setExamStatus(studentExamRegistration.getExamStatus());
                    return studentExamRegistrationRepository.save(existingStudentExamRegistration);
                });
    }

    @Override
    public Optional<StudentExamRegistration> deleteById(Long id) {
        Optional<StudentExamRegistration> studentExamRegistration = findById(id);
        studentExamRegistration.ifPresent(studentExamRegistrationRepository::delete);
        return studentExamRegistration;
    }

    @Override
    public StudentExamRegistration register(Long studentId, Long examId) {
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Exam exam = examService.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        boolean exists = studentExamRegistrationRepository.existsByStudentIdAndExamId(studentId, examId);
        if (exists) {
            throw new RuntimeException("Already registered for this exam");
        }

        StudentExamRegistration reg = new StudentExamRegistration(student, exam);

        return studentExamRegistrationRepository.save(reg);
    }

    @Override
    public List<StudentExamRegistration> findStudentExamRegistrationByStudentId(Long studentId) {
        return studentExamRegistrationRepository.findStudentExamRegistrationByStudentId(studentId);
    }

    @Override
    public List<StudentExamRegistration> findAllByExamId(Long examId) {
        return studentExamRegistrationRepository.findAllByExamId(examId);
    }

    @Override
    public List<StudentExamRegistration> findAllByExamIdAndExamStatus(Long examId, ExamStatus status) {
        return studentExamRegistrationRepository.findAllByExamIdAndExamStatus(examId, status);
    }

}
