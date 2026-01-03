package mk.ukim.finki.emc.academic_assessment_system_backend.config.initialization;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.AcademicRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.CourseRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.ExamRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.StudentRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataInitializer {

    private final CourseRepository courseRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CourseRepository courseRepository, ExamRepository examRepository, UserRepository userRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.courseRepository = courseRepository;
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        User user = new User("Dimitar", "Trposki", "dt@gmail.com", passwordEncoder.encode("dt"), AcademicRole.STAFF);
        User user1 = new User("Dimitar1", "Trposki1", "dt1@gmail.com", passwordEncoder.encode("dt1"), AcademicRole.STUDENT);
        userRepository.save(user);
        userRepository.save(user1);

        Course course = new Course("VP - 2025/2026", "VP", 8, 2025);
        courseRepository.save(course);

        Exam exam = new Exam(
                "Januari",
                LocalDate.of(2026, 1, 15),
                200,
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                course,
                List.of("117", "215"));
        examRepository.save(exam);

        Student student = new Student("221033", "SIIS", user1);
        studentRepository.save(student);
    }

}
