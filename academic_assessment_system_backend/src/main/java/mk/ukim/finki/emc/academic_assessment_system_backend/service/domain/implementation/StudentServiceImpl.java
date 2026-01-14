package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.StudentRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;

    public StudentServiceImpl(StudentRepository studentRepository, UserService userService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> update(Long id, Student student) {
        return findById(id)
                .map(existingStudent -> {
                    existingStudent.setUser(student.getUser());
                    existingStudent.setStudentIndex(student.getStudentIndex());
                    existingStudent.setMajor(student.getMajor());
                    return studentRepository.save(existingStudent);
                });
    }

    @Override
    public Optional<Student> deleteByIdWithUser(Long id) {
        Optional<Student> student = findById(id);
        Optional<User> user = userService.findById(student.get().getUser().getId());
        user.ifPresent(value -> userService.deleteById(value.getId()));
        student.ifPresent(studentRepository::delete);
        return student;
    }

    @Override
    public Optional<Student> deleteByIdWithoutUser(Long id) {
        Optional<Student> student = findById(id);
        student.ifPresent(studentRepository::delete);
        return student;
    }

    @Override
    public Optional<Student> findByStudentIndex(String studentIndex) {
        return studentRepository.findByStudentIndex(studentIndex);
    }
}
