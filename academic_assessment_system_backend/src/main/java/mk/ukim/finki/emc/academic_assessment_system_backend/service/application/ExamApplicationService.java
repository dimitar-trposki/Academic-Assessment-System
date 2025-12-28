package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateExamDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayExamDto;

import java.util.List;
import java.util.Optional;

public interface ExamApplicationService {

    List<DisplayExamDto> findAll();

    Optional<DisplayExamDto> findById(Long id);

    DisplayExamDto save(CreateExamDto createExamDto);

    Optional<DisplayExamDto> update(Long id, CreateExamDto createExamDto);

    Optional<DisplayExamDto> deleteById(Long id);

}
