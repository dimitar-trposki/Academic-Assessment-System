package mk.ukim.finki.emc.academic_assessment_system_backend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.StaffRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "course_staff_assignments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_course_user_staffrole",
                        columnNames = {"course_id", "user_id", "staff_role"}
                )
        }
)
public class CourseStaffAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_course_staff_user")
    )
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_course_staff_course")
    )
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_role", nullable = false, length = 20)
    private StaffRole staffRole;

    public CourseStaffAssignment(User user, Course course, StaffRole staffRole) {
        this.user = user;
        this.course = course;
        this.staffRole = staffRole;
    }
}
