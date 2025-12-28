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
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_student_index", columnNames = "student_index"),
                @UniqueConstraint(name = "uk_student_user", columnNames = "user_id")
        }
)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_index", nullable = false, length = 30)
    private String studentIndex;

    @Column(nullable = false, length = 120)
    private String major;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_student_user")
    )
    private User user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentExamRegistration> examRegistrations;

    public Student(String studentIndex, String major, User user) {
        this.studentIndex = studentIndex;
        this.major = major;
        this.user = user;
        this.examRegistrations = new ArrayList<>();
    }

}