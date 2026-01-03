package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.CourseRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> findAllWithStaff() {
        return courseRepository.findAllWithStaff();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> findByIdWithStaff(Long id) {
        return courseRepository.findByIdWithStaff(id);
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> update(Long id, Course course) {
        return findById(id)
                .map(existingCourse -> {
                    existingCourse.setCourseCode(course.getCourseCode());
                    existingCourse.setCourseName(course.getCourseName());
                    existingCourse.setSemester(course.getSemester());
                    existingCourse.setAcademicYear(course.getAcademicYear());
                    return courseRepository.save(existingCourse);
                });
    }

    @Override
    public Optional<Course> deleteById(Long id) {
        Optional<Course> course = findById(id);
        course.ifPresent(courseRepository::delete);
        return course;
    }
}
