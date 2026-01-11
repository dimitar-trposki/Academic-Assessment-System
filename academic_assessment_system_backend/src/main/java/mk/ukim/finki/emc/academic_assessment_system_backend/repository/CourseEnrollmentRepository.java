package mk.ukim.finki.emc.academic_assessment_system_backend.repository;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {

    boolean existsByCourse_IdAndStudent_Id(Long courseId, Long studentId);

    List<CourseEnrollment> findAllByCourse_Id(Long courseId);

    @Query("""
                SELECT ce FROM CourseEnrollment ce
                JOIN FETCH ce.student s
                JOIN FETCH s.user u
                WHERE ce.course.id = :courseId
            """)
    List<CourseEnrollment> findAllByCourseIdWithStudentAndUser(@Param("courseId") Long courseId);

    List<CourseEnrollment> findAllByStudent_Id(Long studentId);

}
