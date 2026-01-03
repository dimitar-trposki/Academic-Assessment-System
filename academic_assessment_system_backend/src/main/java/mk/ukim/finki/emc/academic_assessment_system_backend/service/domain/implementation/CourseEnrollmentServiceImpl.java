package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.CourseEnrollmentRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseEnrollmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    private final CourseEnrollmentRepository courseEnrollmentRepository;

    public CourseEnrollmentServiceImpl(CourseEnrollmentRepository courseEnrollmentRepository) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
    }

    @Override
    public List<CourseEnrollment> findAll() {
        return courseEnrollmentRepository.findAll();
    }

    @Override
    public Optional<CourseEnrollment> findById(Long id) {
        return courseEnrollmentRepository.findById(id);
    }

    @Override
    public CourseEnrollment save(CourseEnrollment courseEnrollment) {
        return courseEnrollmentRepository.save(courseEnrollment);
    }

    @Override
    public Optional<CourseEnrollment> update(Long id, CourseEnrollment courseEnrollment) {
        return findById(id)
                .map(existingCourseEnrollment -> {
                    existingCourseEnrollment.setStudent(courseEnrollment.getStudent());
                    existingCourseEnrollment.setCourse(courseEnrollment.getCourse());
                    return courseEnrollmentRepository.save(existingCourseEnrollment);
                });
    }

    @Override
    public Optional<CourseEnrollment> deleteById(Long id) {
        Optional<CourseEnrollment> courseEnrollment = findById(id);
        courseEnrollment.ifPresent(courseEnrollmentRepository::delete);
        return courseEnrollment;
    }

    @Override
    public List<CourseEnrollment> findAllByCourseId(Long courseId) {
        return courseEnrollmentRepository.findAllByCourse_Id(courseId);
    }

    @Override
    public List<CourseEnrollment> findAllByCourseIdWithStudentAndUser(Long courseId) {
        return courseEnrollmentRepository.findAllByCourseIdWithStudentAndUser(courseId);
    }

    @Override
    public boolean exists(Long courseId, Long studentId) {
        return courseEnrollmentRepository.existsByCourse_IdAndStudent_Id(courseId, studentId);
    }

}
