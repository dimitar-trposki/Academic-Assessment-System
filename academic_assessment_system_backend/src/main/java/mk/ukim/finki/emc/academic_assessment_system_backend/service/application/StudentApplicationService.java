package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentDto;

import java.util.List;
import java.util.Optional;

public interface StudentApplicationService {

    List<DisplayStudentDto> findAll();

    Optional<DisplayStudentDto> findById(Long id);

    DisplayStudentDto save(CreateStudentDto createStudentDto);

    Optional<DisplayStudentDto> update(Long id, CreateStudentDto createStudentDto);

    Optional<DisplayStudentDto> deleteById(Long id);

}
