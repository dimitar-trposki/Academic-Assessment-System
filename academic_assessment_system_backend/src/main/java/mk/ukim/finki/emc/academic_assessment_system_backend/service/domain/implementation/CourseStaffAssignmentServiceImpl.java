package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.CourseStaffAssignmentRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseStaffAssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseStaffAssignmentServiceImpl implements CourseStaffAssignmentService {

    private final CourseStaffAssignmentRepository courseStaffAssignmentRepository;

    public CourseStaffAssignmentServiceImpl(CourseStaffAssignmentRepository courseStaffAssignmentRepository) {
        this.courseStaffAssignmentRepository = courseStaffAssignmentRepository;
    }

    @Override
    public List<CourseStaffAssignment> findAll() {
        return courseStaffAssignmentRepository.findAll();
    }

    @Override
    public Optional<CourseStaffAssignment> findById(Long id) {
        return courseStaffAssignmentRepository.findById(id);
    }

    @Override
    public CourseStaffAssignment save(CourseStaffAssignment courseStaffAssignment) {
        return courseStaffAssignmentRepository.save(courseStaffAssignment);
    }

    @Override
    public Optional<CourseStaffAssignment> update(Long id, CourseStaffAssignment courseStaffAssignment) {
        return findById(id)
                .map(existingCourseStaffAssignment -> {
                    existingCourseStaffAssignment.setUser(courseStaffAssignment.getUser());
                    existingCourseStaffAssignment.setCourse(courseStaffAssignment.getCourse());
                    existingCourseStaffAssignment.setStaffRole(courseStaffAssignment.getStaffRole());
                    return courseStaffAssignmentRepository.save(existingCourseStaffAssignment);
                });
    }

    @Override
    public Optional<CourseStaffAssignment> deleteById(Long id) {
        Optional<CourseStaffAssignment> courseStaffAssignment = findById(id);
        courseStaffAssignment.ifPresent(courseStaffAssignmentRepository::delete);
        return courseStaffAssignment;
    }

    @Override
    public List<CourseStaffAssignment> findByCourseId(Long courseId) {
        return courseStaffAssignmentRepository.findByCourseId(courseId);
    }

    @Override
    public void deleteByCourseId(Long courseId) {
        courseStaffAssignmentRepository.deleteByCourseId(courseId);
    }
}
