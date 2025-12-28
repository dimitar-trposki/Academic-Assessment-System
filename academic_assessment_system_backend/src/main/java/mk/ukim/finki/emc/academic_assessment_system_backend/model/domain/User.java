package mk.ukim.finki.emc.academic_assessment_system_backend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.AcademicRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "app_users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_app_users_email", columnNames = "email")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_role", nullable = false, length = 20)
    private AcademicRole academicRole;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
        if (student != null) {
            student.setUser(this);
        }
    }

    public User(String firstName, String lastName, String email, String password, AcademicRole academicRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.academicRole = academicRole;
    }

}
