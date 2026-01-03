package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<Student> findAll();

    Optional<Student> findById(Long id);

    Student save(Student student);

    Optional<Student> update(Long id, Student student);

    Optional<Student> deleteById(Long id);

    Optional<Student> findByStudentIndex(String studentIndex);

}
