package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseDto;

import java.util.List;
import java.util.Optional;

public interface CourseApplicationService {

    List<DisplayCourseDto> findAll();

    Optional<DisplayCourseDto> findById(Long id);

    DisplayCourseDto save(CreateCourseDto createCourseDto);

    Optional<DisplayCourseDto> update(Long id, CreateCourseDto createCourseDto);

    Optional<DisplayCourseDto> deleteById(Long id);

}
