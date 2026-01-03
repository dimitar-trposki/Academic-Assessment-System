package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CourseEnrollmentApplicationService {

    List<DisplayCourseEnrollmentDto> findAll();

    Optional<DisplayCourseEnrollmentDto> findById(Long id);

    DisplayCourseEnrollmentDto save(CreateCourseEnrollmentDto createCourseEnrollmentDto);

    Optional<DisplayCourseEnrollmentDto> update(Long id, CreateCourseEnrollmentDto createCourseEnrollmentDto);

    Optional<DisplayCourseEnrollmentDto> deleteById(Long id);

    byte[] exportStudentsCsv(Long courseId);

    int importStudentsCsv(Long courseId, MultipartFile file);

}
