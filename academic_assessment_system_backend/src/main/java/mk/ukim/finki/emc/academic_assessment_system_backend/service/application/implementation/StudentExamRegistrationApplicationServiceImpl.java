package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.AcademicRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentExamRegistrationApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.ExamService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentExamRegistrationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentExamRegistrationApplicationServiceImpl implements StudentExamRegistrationApplicationService {

    private final StudentExamRegistrationService studentExamRegistrationService;
    private final StudentService studentService;
    private final ExamService examService;
    private final UserService userService;

    public StudentExamRegistrationApplicationServiceImpl(StudentExamRegistrationService studentExamRegistrationService, StudentService studentService, ExamService examService, UserService userService) {
        this.studentExamRegistrationService = studentExamRegistrationService;
        this.studentService = studentService;
        this.examService = examService;
        this.userService = userService;
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

    @Override
    public DisplayStudentExamRegistrationDto registerCurrentStudent(String email, Long examId) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAcademicRole() != AcademicRole.STUDENT) {
            throw new RuntimeException("Only STUDENT can register for exams");
        }

        Student student = user.getStudent();
        if (student == null) {
            throw new RuntimeException("Student profile not found for this user");
        }

        return DisplayStudentExamRegistrationDto.from(studentExamRegistrationService.register(student.getId(), examId));
    }

    @Override
    public List<DisplayStudentExamRegistrationDto> findStudentExamRegistrationByStudentId(Long studentId) {
        return DisplayStudentExamRegistrationDto.from(studentExamRegistrationService.findStudentExamRegistrationByStudentId(studentId));
    }

    @Override
    public List<DisplayStudentExamRegistrationDto> findAllByExamId(Long examId) {
        return DisplayStudentExamRegistrationDto.from(studentExamRegistrationService.findAllByExamId(examId));
    }

    @Override
    public List<DisplayStudentExamRegistrationDto> findAllByExamIdAndExamStatus(Long examId, ExamStatus status) {
        return DisplayStudentExamRegistrationDto.from(studentExamRegistrationService.findAllByExamIdAndExamStatus(examId, status));
    }

    @Override
    public byte[] exportStudentsByExamStatusCsv(Long examId, ExamStatus examStatus) {
        List<StudentExamRegistration> regs = studentExamRegistrationService.findAllByExamIdAndExamStatus(examId, examStatus);

        StringBuilder sb = new StringBuilder();
        sb.append("studentIndex,studentMajor,firstName,lastName,examStatus\n");

        for (StudentExamRegistration r : regs) {
            Student student = r.getStudent();
            User user = student.getUser();
            sb.append(student.getStudentIndex()).append(',')
                    .append(escapeCsv(student.getMajor())).append(',')
                    .append(escapeCsv(user.getFirstName())).append(',')
                    .append(escapeCsv(user.getLastName())).append(',')
                    .append(r.getExamStatus().name())
                    .append('\n');
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Transactional
    @Override
    public int importStudentsCsv(Long examId, MultipartFile file) {

        List<StudentExamRegistration> allRegs =
                studentExamRegistrationService.findAllByExamId(examId);

        Map<String, StudentExamRegistration> regByStudentIndex = allRegs.stream()
                .collect(Collectors.toMap(
                        r -> r.getStudent().getStudentIndex(),
                        r -> r
                ));

        Set<String> attendedIndexes = new HashSet<>();
        int validLines = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        )) {
            String line = br.readLine();
            if (line == null) return 0;

            boolean hasHeader = line.toLowerCase().contains("studentindex");
            if (!hasHeader) {
                if (consumeLine(line, attendedIndexes)) validLines++;
            }

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                if (consumeLine(line, attendedIndexes)) validLines++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to import CSV", e);
        }

        for (String studentIndex : attendedIndexes) {
            StudentExamRegistration reg = regByStudentIndex.get(studentIndex);
            if (reg == null) continue;
            if (reg.getExamStatus() != ExamStatus.ATTENDED) {
                reg.setExamStatus(ExamStatus.ATTENDED);
            }
        }

        for (StudentExamRegistration reg : allRegs) {
            if (reg.getExamStatus() == ExamStatus.REGISTERED) {
                String studentIndex = reg.getStudent().getStudentIndex();
                if (!attendedIndexes.contains(studentIndex)) {
                    reg.setExamStatus(ExamStatus.ABSENT);
                }
            }
        }

        for (StudentExamRegistration reg : allRegs) {
            studentExamRegistrationService.save(reg);
        }

        return validLines;
    }

    private boolean consumeLine(String line, Set<String> attendedIds) {
        try {
            String[] parts = line.split(",", -1);
            String first = parts[0].trim().replace("\"", "");
            if (first.isBlank()) return false;
            String studentIndex = first.trim().replace("\"", "");
            attendedIds.add(studentIndex);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private String escapeCsv(String v) {
        if (v == null) return "";
        boolean needsQuotes = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
        String val = v.replace("\"", "\"\"");
        return needsQuotes ? ("\"" + val + "\"") : val;
    }

}
