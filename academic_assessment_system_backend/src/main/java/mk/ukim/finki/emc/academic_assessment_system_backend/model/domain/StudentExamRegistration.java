package mk.ukim.finki.emc.academic_assessment_system_backend.model.domain;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "student_exam_registrations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_exam_student_registration",
                        columnNames = {"exam_id", "student_id"}
                )
        }
)
public class StudentExamRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ser_student")
    )
    private Student student;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "exam_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ser_exam")
    )
    private Exam exam;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_status", nullable = false, length = 20)
    private ExamStatus examStatus;

    public StudentExamRegistration(Student student, Exam exam) {
        this.student = student;
        this.exam = exam;
        this.examStatus = ExamStatus.REGISTERED;
    }

}
