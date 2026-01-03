package mk.ukim.finki.emc.academic_assessment_system_backend.repository;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
                SELECT DISTINCT c FROM Course c
                LEFT JOIN FETCH c.courseStaffAssignments csa
                LEFT JOIN FETCH csa.user
            """)
    List<Course> findAllWithStaff();

    @Query("""
                SELECT DISTINCT c FROM Course c
                LEFT JOIN FETCH c.courseStaffAssignments csa
                LEFT JOIN FETCH csa.user
                WHERE c.id = :id
            """)
    Optional<Course> findByIdWithStaff(@Param("id") Long id);

}
