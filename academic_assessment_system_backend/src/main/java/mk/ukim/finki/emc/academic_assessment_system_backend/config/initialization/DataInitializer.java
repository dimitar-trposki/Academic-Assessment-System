package mk.ukim.finki.emc.academic_assessment_system_backend.config.initialization;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.*;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.StaffRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.*;
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
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final CourseStaffAssignmentRepository courseStaffAssignmentRepository;
    private final StudentExamRegistrationRepository studentExamRegistrationRepository;

    public DataInitializer(CourseRepository courseRepository, ExamRepository examRepository, UserRepository userRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder, CourseEnrollmentRepository courseEnrollmentRepository, CourseStaffAssignmentRepository courseStaffAssignmentRepository, StudentExamRegistrationRepository studentExamRegistrationRepository) {
        this.courseRepository = courseRepository;
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseStaffAssignmentRepository = courseStaffAssignmentRepository;
        this.studentExamRegistrationRepository = studentExamRegistrationRepository;
    }

    @PostConstruct
    public void init() {
        User user = new User("Dimitar", "Trposki", "dt@gmail.com", passwordEncoder.encode("dt"), UserRole.STAFF);
        User user1 = new User("Jovan", "Jovanov", "jj@gmail.com", passwordEncoder.encode("jj"), UserRole.STAFF);
        User user2 = new User("Ivan", "Ivanov", "ii@gmail.com", passwordEncoder.encode("ii"), UserRole.STUDENT);
        User user3 = new User("admin", "admin", "admin@gmail.com", passwordEncoder.encode("admin"), UserRole.ADMINISTRATOR);
        User user4 = new User("user", "user", "user@gmail.com", passwordEncoder.encode("user"), UserRole.USER);
        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        Course course = new Course("VP - 2025/2026", "VP", 7, 2025);
        Course course1 = new Course("VNP - 2024/2025", "VNP", 5, 2024);
        Course course2 = new Course("SKIT - 2024/2025", "SKIT", 6, 2024);
        courseRepository.save(course);
        courseRepository.save(course1);
        courseRepository.save(course2);

        Exam exam = new Exam(
                "Januari",
                LocalDate.of(2026, 1, 15),
                200,
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                course,
                List.of("117", "215"));
        Exam exam1 = new Exam(
                "Juni",
                LocalDate.of(2026, 6, 15),
                150,
                LocalTime.of(10, 0),
                LocalTime.of(13, 0),
                course1,
                List.of("117", "215", "200ab"));
        Exam exam2 = new Exam(
                "Septemvri",
                LocalDate.of(2026, 9, 15),
                100,
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                course2,
                List.of("215", "200ab"));
        examRepository.save(exam);
        examRepository.save(exam1);
        examRepository.save(exam2);

        Student student = new Student("221033", "SIIS", user2);
        studentRepository.save(student);

        CourseEnrollment courseEnrollment = new CourseEnrollment(student, course);
        courseEnrollmentRepository.save(courseEnrollment);

        CourseStaffAssignment courseStaffAssignment = new CourseStaffAssignment(user, course, StaffRole.PROFESSOR);
        CourseStaffAssignment courseStaffAssignment1 = new CourseStaffAssignment(user1, course, StaffRole.ASSISTANT);
        courseStaffAssignmentRepository.save(courseStaffAssignment);
        courseStaffAssignmentRepository.save(courseStaffAssignment1);

        StudentExamRegistration studentExamRegistration = new StudentExamRegistration(student, exam);
        studentExamRegistrationRepository.save(studentExamRegistration);
    }

}
