package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseEnrollmentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseEnrollmentService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class CourseEnrollmentApplicationServiceImpl implements CourseEnrollmentApplicationService {

    private final CourseEnrollmentService courseEnrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public CourseEnrollmentApplicationServiceImpl(CourseEnrollmentService courseEnrollmentService, StudentService studentService, CourseService courseService) {
        this.courseEnrollmentService = courseEnrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @Override
    public List<DisplayCourseEnrollmentDto> findAll() {
        return DisplayCourseEnrollmentDto
                .from(courseEnrollmentService.findAll());
    }

    @Override
    public Optional<DisplayCourseEnrollmentDto> findById(Long id) {
        return courseEnrollmentService
                .findById(id)
                .map(DisplayCourseEnrollmentDto::from);
    }

    @Override
    public DisplayCourseEnrollmentDto save(CreateCourseEnrollmentDto createCourseEnrollmentDto) {
        Optional<Student> student = studentService.findById(createCourseEnrollmentDto.studentId());
        Optional<Course> course = courseService.findById(createCourseEnrollmentDto.courseId());

        return DisplayCourseEnrollmentDto
                .from(courseEnrollmentService.save(createCourseEnrollmentDto.toCourseEnrollment(student.get(), course.get())));
    }

    @Override
    public Optional<DisplayCourseEnrollmentDto> update(Long id, CreateCourseEnrollmentDto createCourseEnrollmentDto) {
        Optional<Student> student = studentService.findById(createCourseEnrollmentDto.studentId());
        Optional<Course> course = courseService.findById(createCourseEnrollmentDto.courseId());

        return courseEnrollmentService
                .update(id, createCourseEnrollmentDto.toCourseEnrollment(student.get(), course.get()))
                .map(DisplayCourseEnrollmentDto::from);
    }

    @Override
    public Optional<DisplayCourseEnrollmentDto> deleteById(Long id) {
        return courseEnrollmentService
                .deleteById(id)
                .map(DisplayCourseEnrollmentDto::from);
    }

    @Override
    public byte[] exportStudentsCsv(Long courseId) {
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<CourseEnrollment> enrollments =
                courseEnrollmentService.findAllByCourseIdWithStudentAndUser(courseId);

        StringBuilder sb = new StringBuilder();
        sb.append("studentIndex,firstName,lastName,major,email,academicRole\n");

        for (CourseEnrollment ce : enrollments) {
            Student s = ce.getStudent();
            User u = s.getUser();

            sb.append(escapeCsv(s.getStudentIndex())).append(",");
            sb.append(escapeCsv(u.getFirstName())).append(",");
            sb.append(escapeCsv(u.getLastName())).append(",");
            sb.append(escapeCsv(s.getMajor())).append(",");
            sb.append(escapeCsv(u.getEmail())).append(",");
            sb.append(escapeCsv(u.getAcademicRole() != null ? u.getAcademicRole().name() : "")).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public int importStudentsCsv(Long courseId, MultipartFile file) {
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CSV file is required");
        }

        int created = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        )) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (firstLine) {
                    firstLine = false;
                    if (line.toLowerCase().contains("studentindex")) {
                        continue;
                    }
                }

                String[] parts = line.split(",", -1);
                String studentIndex = parts[0].trim();

                if (studentIndex.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Empty studentIndex in row: " + line);
                }

                Student student = studentService.findByStudentIndex(studentIndex)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Student not found for index: " + studentIndex
                        ));

                if (courseEnrollmentService.exists(courseId, student.getId())) {
                    continue;
                }

                CourseEnrollment ce = new CourseEnrollment();
                ce.setCourse(course);
                ce.setStudent(student);

                courseEnrollmentService.save(ce);
                created++;
            }

            return created;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read CSV file");
        }
    }

    @Override
    public List<DisplayCourseEnrollmentDto> findAllByCourseIdWithStudentAndUser(Long courseId) {
        return DisplayCourseEnrollmentDto.from(courseEnrollmentService.findAllByCourseIdWithStudentAndUser(courseId));
    }

    @Override
    public List<DisplayCourseEnrollmentDto> findAllByStudentId(Long studentId) {
        return DisplayCourseEnrollmentDto.from(courseEnrollmentService.findAllByStudentId(studentId));
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        String v = value;
        boolean needsQuotes = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
        v = v.replace("\"", "\"\"");
        return needsQuotes ? "\"" + v + "\"" : v;
    }

}
