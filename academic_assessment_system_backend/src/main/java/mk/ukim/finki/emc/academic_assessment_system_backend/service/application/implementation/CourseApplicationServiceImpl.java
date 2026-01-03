package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import jakarta.transaction.Transactional;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.helpers.StaffAssignmentHelper;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseStaffAssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseApplicationServiceImpl implements CourseApplicationService {

    private final CourseService courseService;
    private final CourseStaffAssignmentService courseStaffAssignmentService;
    private final StaffAssignmentHelper staffAssignmentHelper;

    public CourseApplicationServiceImpl(CourseService courseService, CourseStaffAssignmentService courseStaffAssignmentService, StaffAssignmentHelper staffAssignmentHelper) {
        this.courseService = courseService;
        this.courseStaffAssignmentService = courseStaffAssignmentService;
        this.staffAssignmentHelper = staffAssignmentHelper;
    }

    @Override
    public List<DisplayCourseDto> findAll() {
        return DisplayCourseDto
                .from(courseService.findAll());
    }

    @Override
    public List<DisplayCourseDto> findAllWithStaff() {
        return DisplayCourseDto
                .from(courseService.findAllWithStaff());
    }

    @Override
    public Optional<DisplayCourseDto> findById(Long id) {
        return courseService
                .findById(id)
                .map(DisplayCourseDto::from);
    }

    @Override
    public Optional<DisplayCourseDto> findByIdWithStaff(Long id) {
        return courseService
                .findByIdWithStaff(id)
                .map(DisplayCourseDto::from);
    }

//    @Override
//    public DisplayCourseDto save(CreateCourseDto createCourseDto) {
//        return DisplayCourseDto
//                .from(courseService.save(createCourseDto.toCourse()));
//    }

    @Override
    @Transactional
    public DisplayCourseDto save(CreateCourseDto createCourseDto) {
        Course course = courseService.save(createCourseDto.toCourse());

        staffAssignmentHelper.applyStaffAssignments(course, createCourseDto.professorIds(), createCourseDto.assistantIds());

        Course reloaded = courseService.findByIdWithStaff(course.getId())
                .orElseThrow(() -> new RuntimeException("Course not found after save: " + course.getId()));

        return DisplayCourseDto.from(reloaded);
    }

//    @Override
//    public Optional<DisplayCourseDto> update(Long id, CreateCourseDto createCourseDto) {
//        return courseService
//                .update(id, createCourseDto.toCourse())
//                .map(DisplayCourseDto::from);
//    }

    @Override
    @Transactional
    public Optional<DisplayCourseDto> update(Long id, CreateCourseDto createCourseDto) {
        Course course = courseService.findByIdWithStaff(id).orElse(null);
        if (course == null) return Optional.empty();

        courseService.update(id, createCourseDto.toCourse());

        courseStaffAssignmentService.deleteByCourseId(id);

        staffAssignmentHelper.applyStaffAssignments(course, createCourseDto.professorIds(), createCourseDto.assistantIds());

        Course reloaded = courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found after update: " + id));

        return Optional.of(DisplayCourseDto.from(reloaded));
    }

//    @Override
//    public Optional<DisplayCourseDto> deleteById(Long id) {
//        return courseService
//                .deleteById(id)
//                .map(DisplayCourseDto::from);
//    }

    @Override
    @Transactional
    public Optional<DisplayCourseDto> deleteById(Long id) {
        Course course = courseService.findById(id).orElse(null);
        if (course == null) return Optional.empty();

        courseStaffAssignmentService.deleteByCourseId(id);

        courseService.deleteById(id);
        return Optional.of(DisplayCourseDto.from(course));
    }
}
