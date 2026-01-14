package mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;

import java.util.List;

public record CreateUserDto(
        @NotBlank(message = "First name is required")
        @Size(max = 80, message = "First name must be at most 80 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 80, message = "Last name must be at most 80 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must be at most 150 characters")
        String email,

//        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,

        @NotNull(message = "User role is required")
        UserRole userRole
) {

    public User toUser() {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(userRole);
        return user;
    }

    public static CreateUserDto from(User user) {
        return new CreateUserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                null, // never expose password
                user.getUserRole()
        );
    }

    public static List<CreateUserDto> from(List<User> users) {
        return users.stream()
                .map(CreateUserDto::from)
                .toList();
    }
}
