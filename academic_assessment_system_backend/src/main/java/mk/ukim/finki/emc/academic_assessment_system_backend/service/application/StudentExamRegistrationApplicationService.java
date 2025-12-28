package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentExamRegistrationDto;

import java.util.List;
import java.util.Optional;

public interface StudentExamRegistrationApplicationService {

    List<DisplayStudentExamRegistrationDto> findAll();

    Optional<DisplayStudentExamRegistrationDto> findById(Long id);

    DisplayStudentExamRegistrationDto save(CreateStudentExamRegistrationDto createStudentExamRegistrationDto);

    Optional<DisplayStudentExamRegistrationDto> update(Long id, CreateStudentExamRegistrationDto createStudentExamRegistrationDto);

    Optional<DisplayStudentExamRegistrationDto> deleteById(Long id);

}
