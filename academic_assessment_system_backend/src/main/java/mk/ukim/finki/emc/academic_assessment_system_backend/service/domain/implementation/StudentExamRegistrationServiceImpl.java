package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.StudentExamRegistration;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.StudentExamRegistrationRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentExamRegistrationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentExamRegistrationServiceImpl implements StudentExamRegistrationService {

    private final StudentExamRegistrationRepository studentExamRegistrationRepository;

    public StudentExamRegistrationServiceImpl(StudentExamRegistrationRepository studentExamRegistrationRepository) {
        this.studentExamRegistrationRepository = studentExamRegistrationRepository;
    }

    @Override
    public List<StudentExamRegistration> findAll() {
        return studentExamRegistrationRepository.findAll();
    }

    @Override
    public Optional<StudentExamRegistration> findById(Long id) {
        return studentExamRegistrationRepository.findById(id);
    }

    @Override
    public StudentExamRegistration save(StudentExamRegistration studentExamRegistration) {
        return studentExamRegistrationRepository.save(studentExamRegistration);
    }

    @Override
    public Optional<StudentExamRegistration> update(Long id, StudentExamRegistration studentExamRegistration) {
        return findById(id)
                .map(existingStudentExamRegistration -> {
                    existingStudentExamRegistration.setStudent(studentExamRegistration.getStudent());
                    existingStudentExamRegistration.setExam(studentExamRegistration.getExam());
                    existingStudentExamRegistration.setExamStatus(studentExamRegistration.getExamStatus());
                    return studentExamRegistrationRepository.save(existingStudentExamRegistration);
                });
    }

    @Override
    public Optional<StudentExamRegistration> deleteById(Long id) {
        Optional<StudentExamRegistration> studentExamRegistration = findById(id);
        studentExamRegistration.ifPresent(studentExamRegistrationRepository::delete);
        return studentExamRegistration;
    }
}
