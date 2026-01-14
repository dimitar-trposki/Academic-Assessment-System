package mk.ukim.finki.emc.academic_assessment_system_backend.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String session;

    @Column(nullable = false)
    private LocalDate dateOfExam;

    @Column(nullable = false)
    private Integer capacityOfStudents;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "exam_reserved_labs",
            joinColumns = @JoinColumn(name = "exam_id")
    )
    @Column(name = "laboratory", nullable = false, length = 100)
    private List<String> reservedLaboratories;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_exam_course")
    )
    private Course course;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentExamRegistration> studentExamRegistrations;

    public Exam(String session, LocalDate dateOfExam, Integer capacityOfStudents, LocalTime startTime, LocalTime endTime, Course course) {
        this.session = session;
        this.dateOfExam = dateOfExam;
        this.capacityOfStudents = capacityOfStudents;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
        this.reservedLaboratories = new ArrayList<>();
        this.studentExamRegistrations = new ArrayList<>();
    }

    public Exam(String session, LocalDate dateOfExam, Integer capacityOfStudents, LocalTime startTime, LocalTime endTime, Course course, List<String> reservedLaboratories) {
        this.session = session;
        this.dateOfExam = dateOfExam;
        this.capacityOfStudents = capacityOfStudents;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
        this.reservedLaboratories = reservedLaboratories;
        this.studentExamRegistrations = new ArrayList<>();
    }
}
