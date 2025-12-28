package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.ExamRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.ExamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    @Override
    public Optional<Exam> findById(Long id) {
        return examRepository.findById(id);
    }

    @Override
    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Optional<Exam> update(Long id, Exam exam) {
        return findById(id)
                .map(existingExam -> {
                    existingExam.setSession(exam.getSession());
                    existingExam.setDateOfExam(exam.getDateOfExam());
                    existingExam.setCapacityOfStudents(exam.getCapacityOfStudents());
                    existingExam.setReservedLaboratories(exam.getReservedLaboratories());
                    existingExam.setStartTime(exam.getStartTime());
                    existingExam.setEndTime(exam.getEndTime());
                    return examRepository.save(existingExam);
                });
    }

    @Override
    public Optional<Exam> deleteById(Long id) {
        Optional<Exam> exam = findById(id);
        exam.ifPresent(examRepository::delete);
        return exam;
    }
}
