package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseEnrollmentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseEnrollmentService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentService;
import org.springframework.stereotype.Service;

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
}
