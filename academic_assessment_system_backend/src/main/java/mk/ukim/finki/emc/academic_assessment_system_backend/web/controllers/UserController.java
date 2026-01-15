package mk.ukim.finki.emc.academic_assessment_system_backend.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.LoginUserRequestDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.LoginUserResponseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.RegisterUserRequestDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.RegisterUserResponseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateUserDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayUserDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayUserStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.security.JwtUserPrincipal;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseStaffAssignmentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.PasswordResetApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.UserApplicationService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/users",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Users",
        description = "Operations related to application users"
)
public class UserController {

    private final UserApplicationService userApplicationService;
    private final PasswordResetApplicationService passwordResetApplicationService;
    private final CourseStaffAssignmentApplicationService courseStaffAssignmentApplicationService;

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all users in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of users",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayUserDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<DisplayUserDto>> findAll() {
        return ResponseEntity.ok(userApplicationService.findAll());
    }

    @GetMapping("/staff")
    public ResponseEntity<List<DisplayUserDto>> findAllStaff() {
        return ResponseEntity.ok(userApplicationService.findAllStaff());
    }

    @GetMapping("/students")
    public ResponseEntity<List<DisplayUserStudentDto>> findAllStudents() {
        return ResponseEntity.ok(userApplicationService.findAllStudents());
    }

    @Operation(
            summary = "Get current authenticated user (JWT principal)",
            description = "Returns the current authenticated user principal extracted from JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Current principal returned",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JwtUserPrincipal.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized (missing/invalid token)",
                    content = @Content
            )
    })
    @GetMapping("/me")
    public JwtUserPrincipal me(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return principal;
    }
//    @GetMapping("/me")
//    public DisplayMyProfileDto me(@AuthenticationPrincipal JwtUserPrincipal principal) {
//        return userApplicationService.myProfile(principal.id());
//    }

    @Operation(
            summary = "Register new user",
            description = "Registers a new user and returns registration response."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegisterUserResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": 1,
                                              "firstName": "Ana",
                                              "lastName": "Ivanova",
                                              "email": "ana@finki.mk",
                                              "userRole": "STUDENT"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Registration failed / invalid input",
                    content = @Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> register(@RequestBody RegisterUserRequestDto registerUserRequestDto) {
        return userApplicationService
                .register(registerUserRequestDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates a user and returns JWT + user details (depending on your response DTO)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginUserResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJI...",
                                              "expiresIn": 3600
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials / login failed",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> login(@RequestBody LoginUserRequestDto loginUserRequestDto) {
        return userApplicationService
                .login(loginUserRequestDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Import users from CSV",
            description = """
                    Upload a CSV file (multipart/form-data) to import users.
                    
                    Required header:
                    firstName,lastName,email,password,userRole,studentIndex,major
                    
                    Rules:
                    - If userRole=STUDENT: studentIndex and major are required.
                    - If NOT STUDENT: studentIndex and major should be empty.
                    - New user: password required.
                    - Existing user: empty password => password not changed.
                    """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Import finished (created/updated/errors returned)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "object"),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "created": 2,
                                              "updated": 1,
                                              "errors": [
                                                {
                                                  "row": "5",
                                                  "email": "bad@finki.mk",
                                                  "error": "studentIndex and major are required for STUDENT"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid file or CSV format", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized (if endpoint protected)", content = @Content)
    })
    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> importUsers(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(userApplicationService.importUsersFromCsv(file));
    }

    @Operation(
            summary = "Export users to CSV",
            description = """
                    Downloads a CSV file with all users.
                    
                    Header:
                    firstName,lastName,email,password,userRole,studentIndex,major
                    
                    Notes:
                    - password is exported empty for security.
                    - studentIndex/major are filled only for STUDENT users.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV file returned",
                    content = @Content(
                            mediaType = "text/csv",
                            schema = @Schema(type = "string", format = "binary"),
                            examples = @ExampleObject(
                                    value = """
                                            firstName,lastName,email,password,userRole,studentIndex,major
                                            Ana,Ivanova,ana@finki.mk,,STUDENT,201234,Computer Science
                                            Marko,Petrovski,marko@gmail.com,,STAFF,,
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized (if endpoint protected)", content = @Content)
    })
    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportUsers() {
        byte[] csv = userApplicationService.exportUsersToCsv();

        ByteArrayResource resource = new ByteArrayResource(csv);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(csv.length)
                .body(resource);
    }

    @Operation(
            summary = "Request password reset",
            description = "Creates a password reset token for the given email address."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset token created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email is missing or invalid",
                    content = @Content
            )
    })
    @PostMapping("/password-reset/request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody java.util.Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "email is required"));
        }

        String token = passwordResetApplicationService.requestReset(email);

        return ResponseEntity.ok(java.util.Map.of(
                "message", "Reset token created (DEV mode returned).",
                "token", token
        ));
    }

    @Operation(
            summary = "Confirm password reset",
            description = "Resets the user password using a valid reset token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password successfully reset"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Token or new password is missing or invalid",
                    content = @Content
            )
    })
    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> confirmPasswordReset(@RequestBody java.util.Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "token is required"));
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "newPassword is required"));
        }

        passwordResetApplicationService.confirmReset(token, newPassword);

        return ResponseEntity.ok(java.util.Map.of("message", "Password successfully reset"));
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user based on its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayUserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisplayUserDto> findById(@PathVariable Long id) {
        return userApplicationService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new user",
            description = "Creates a new application user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayUserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayUserDto> save(
            @Valid @RequestBody CreateUserDto createUserDto
    ) {
        return ResponseEntity.ok(userApplicationService.save(createUserDto));
    }

    @Operation(
            summary = "Update an existing user",
            description = "Updates the user with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayUserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayUserDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateUserDto createUserDto
    ) {
        return userApplicationService
                .update(id, createUserDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes the user with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayUserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DisplayUserDto> deleteById(@PathVariable Long id) {
        return userApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get assigned courses for staff",
            description = "Returns a list of all assigned courses for the user with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved staff assignments",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseEnrollmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}/assigned-courses")
    public ResponseEntity<List<DisplayCourseStaffAssignmentDto>> findCoursesByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(courseStaffAssignmentApplicationService.findCourseStaffAssignmentByUserId(id));
    }

//    @GetMapping("/me")
//    public ResponseEntity<RegisterUserResponseDto> me(@AuthenticationPrincipal User user) {
//        return userApplicationService
//                .findByEmail(user.getEmail())
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.badRequest().build());
//    }
}
