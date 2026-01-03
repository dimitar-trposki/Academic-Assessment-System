package mk.ukim.finki.emc.academic_assessment_system_backend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "courses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_course_code_semester_year",
                        columnNames = {"courseCode", "semester", "academicYear"}
                )
        }
)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String courseCode;

    @Column(nullable = false, length = 200)
    private String courseName;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private Integer academicYear;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CourseStaffAssignment> courseStaffAssignments = new ArrayList<>();

    public Course(String courseCode, String courseName, Integer semester, Integer academicYear) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.semester = semester;
        this.academicYear = academicYear;
    }
}

