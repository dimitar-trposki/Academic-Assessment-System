package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseApplicationServiceImpl implements CourseApplicationService {

    private final CourseService courseService;

    public CourseApplicationServiceImpl(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public List<DisplayCourseDto> findAll() {
        return DisplayCourseDto
                .from(courseService.findAll());
    }

    @Override
    public Optional<DisplayCourseDto> findById(Long id) {
        return courseService
                .findById(id)
                .map(DisplayCourseDto::from);
    }

    @Override
    public DisplayCourseDto save(CreateCourseDto createCourseDto) {
        return DisplayCourseDto
                .from(courseService.save(createCourseDto.toCourse()));
    }

    @Override
    public Optional<DisplayCourseDto> update(Long id, CreateCourseDto createCourseDto) {
        return courseService
                .update(id, createCourseDto.toCourse())
                .map(DisplayCourseDto::from);
    }

    @Override
    public Optional<DisplayCourseDto> deleteById(Long id) {
        return courseService
                .deleteById(id)
                .map(DisplayCourseDto::from);
    }
}
