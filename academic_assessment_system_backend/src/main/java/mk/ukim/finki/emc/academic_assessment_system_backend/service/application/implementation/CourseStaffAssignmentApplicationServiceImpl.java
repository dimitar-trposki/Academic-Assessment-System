package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseStaffAssignmentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseStaffAssignmentService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseStaffAssignmentApplicationServiceImpl implements CourseStaffAssignmentApplicationService {

    private final CourseStaffAssignmentService courseStaffAssignmentService;
    private final UserService userService;
    private final CourseService courseService;

    public CourseStaffAssignmentApplicationServiceImpl(CourseStaffAssignmentService courseStaffAssignmentService, UserService userService, CourseService courseService) {
        this.courseStaffAssignmentService = courseStaffAssignmentService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public List<DisplayCourseStaffAssignmentDto> findAll() {
        return DisplayCourseStaffAssignmentDto
                .from(courseStaffAssignmentService.findAll());
    }

    @Override
    public Optional<DisplayCourseStaffAssignmentDto> findById(Long id) {
        return courseStaffAssignmentService
                .findById(id)
                .map(DisplayCourseStaffAssignmentDto::from);
    }

    @Override
    public DisplayCourseStaffAssignmentDto save(CreateCourseStaffAssignmentDto createCourseStaffAssignmentDto) {
        Optional<User> user = userService.findById(createCourseStaffAssignmentDto.userId());
        Optional<Course> course = courseService.findById(createCourseStaffAssignmentDto.courseId());

        return DisplayCourseStaffAssignmentDto
                .from(courseStaffAssignmentService.save(createCourseStaffAssignmentDto.toCourseStaffAssignment(user.get(), course.get())));
    }

    @Override
    public Optional<DisplayCourseStaffAssignmentDto> update(Long id, CreateCourseStaffAssignmentDto createCourseStaffAssignmentDto) {
        Optional<User> user = userService.findById(createCourseStaffAssignmentDto.userId());
        Optional<Course> course = courseService.findById(createCourseStaffAssignmentDto.courseId());

        return courseStaffAssignmentService
                .update(id, createCourseStaffAssignmentDto.toCourseStaffAssignment(user.get(), course.get()))
                .map(DisplayCourseStaffAssignmentDto::from);
    }

    @Override
    public Optional<DisplayCourseStaffAssignmentDto> deleteById(Long id) {
        return courseStaffAssignmentService
                .deleteById(id)
                .map(DisplayCourseStaffAssignmentDto::from);
    }

    @Override
    public List<CourseStaffAssignment> findByCourseId(Long courseId) {
        return courseStaffAssignmentService.findByCourseId(courseId);
    }

    @Override
    public void deleteByCourseId(Long courseId) {
        courseStaffAssignmentService.deleteByCourseId(courseId);
    }

    @Override
    public List<DisplayCourseStaffAssignmentDto> findAllByCourseIdWithUser(Long courseId) {
        return DisplayCourseStaffAssignmentDto.from(courseStaffAssignmentService.findAllByCourseIdWithUser(courseId));
    }

    @Override
    public List<DisplayCourseStaffAssignmentDto> findCourseStaffAssignmentByUserId(Long userId) {
        return DisplayCourseStaffAssignmentDto.from(courseStaffAssignmentService.findCourseStaffAssignmentByUserId(userId));
    }

}
