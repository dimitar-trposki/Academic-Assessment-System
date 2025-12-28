package mk.ukim.finki.emc.academic_assessment_system_backend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
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

    @ElementCollection
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

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentExamRegistration> studentExamRegistrations;

    public Exam(String session, LocalDate dateOfExam, Integer capacityOfStudents, LocalTime startTime, LocalTime endTime) {
        this.session = session;
        this.dateOfExam = dateOfExam;
        this.capacityOfStudents = capacityOfStudents;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservedLaboratories = new ArrayList<>();
        this.studentExamRegistrations = new ArrayList<>();
    }

    public Exam(String session, LocalDate dateOfExam, Integer capacityOfStudents, LocalTime startTime, LocalTime endTime, List<String> reservedLaboratories) {
        this.session = session;
        this.dateOfExam = dateOfExam;
        this.capacityOfStudents = capacityOfStudents;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservedLaboratories = reservedLaboratories;
        this.studentExamRegistrations = new ArrayList<>();
    }
}
