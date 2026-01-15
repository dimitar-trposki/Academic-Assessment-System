package mk.ukim.finki.emc.academic_assessment_system_backend.repository;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentIndex(String studentIndex);

    Optional<Student> findStudentByUserId(Long userId);

}
