package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateExamDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayExamDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.ExamApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.ExamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamApplicationServiceImpl implements ExamApplicationService {

    private final ExamService examService;

    public ExamApplicationServiceImpl(ExamService examService) {
        this.examService = examService;
    }

    @Override
    public List<DisplayExamDto> findAll() {
        return DisplayExamDto
                .from(examService.findAll());
    }

    @Override
    public Optional<DisplayExamDto> findById(Long id) {
        return examService
                .findById(id)
                .map(DisplayExamDto::from);
    }

    @Override
    public DisplayExamDto save(CreateExamDto createExamDto) {
        return DisplayExamDto
                .from(examService.save(createExamDto.toExam()));
    }

    @Override
    public Optional<DisplayExamDto> update(Long id, CreateExamDto createExamDto) {
        return examService
                .update(id, createExamDto.toExam())
                .map(DisplayExamDto::from);
    }

    @Override
    public Optional<DisplayExamDto> deleteById(Long id) {
        return examService
                .deleteById(id)
                .map(DisplayExamDto::from);
    }

    @Override
    public List<DisplayExamDto> findAllByCourseId(Long courseId) {
        return DisplayExamDto.from(examService
                .findAllByCourseId(courseId));
    }
}
