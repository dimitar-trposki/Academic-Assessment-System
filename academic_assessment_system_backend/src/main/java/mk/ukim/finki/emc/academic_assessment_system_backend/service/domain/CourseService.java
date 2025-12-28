package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> findAll();

    Optional<Course> findById(Long id);

    Course save(Course course);

    Optional<Course> update(Long id, Course course);

    Optional<Course> deleteById(Long id);

}
