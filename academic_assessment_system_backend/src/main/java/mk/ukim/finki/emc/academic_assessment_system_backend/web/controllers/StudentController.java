package mk.ukim.finki.emc.academic_assessment_system_backend.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseEnrollmentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentExamRegistrationApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/students",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Students",
        description = "Operations related to students and student profiles"
)
public class StudentController {

    private final StudentApplicationService studentApplicationService;
    private final StudentExamRegistrationApplicationService studentExamRegistrationApplicationServiceService;
    private final CourseEnrollmentApplicationService courseEnrollmentApplicationService;

    @Operation(
            summary = "Get all students",
            description = "Returns a list of all students."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of students",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<DisplayStudentDto>> findAll() {
        return ResponseEntity.ok(studentApplicationService.findAll());
    }

    @Operation(
            summary = "Get student by ID",
            description = "Returns a student based on its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisplayStudentDto> findById(@PathVariable Long id) {
        return studentApplicationService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a student profile",
            description = "Creates a student profile linked to an existing user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayStudentDto> save(
            @Valid @RequestBody CreateStudentDto createStudentDto
    ) {
        //return ResponseEntity.ok(studentApplicationService.save(createStudentDto));
        return studentApplicationService
                .save(createStudentDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update a student",
            description = "Updates the student with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayStudentDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateStudentDto createStudentDto
    ) {
        return studentApplicationService
                .update(id, createStudentDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a student with user",
            description = "Deletes the student with the given ID and the associated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}/delete-with-user")
    public ResponseEntity<DisplayStudentDto> deleteByIdWithUser(@PathVariable Long id) {
        return studentApplicationService
                .deleteByIdWithUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a student without user",
            description = "Deletes the student with the given ID without the associated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}/delete-without-user")
    public ResponseEntity<DisplayStudentDto> deleteByIdWithoutUser(@PathVariable Long id) {
        return studentApplicationService
                .deleteByIdWithoutUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get exam registrations for a student",
            description = "Returns a list of exam registrations for the student with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved exam registrations",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}/exam-registrations")
    public ResponseEntity<List<DisplayStudentExamRegistrationDto>> findStudentExamRegistrationByStudentId(@PathVariable Long id) {
        return ResponseEntity.ok(studentExamRegistrationApplicationServiceService.findStudentExamRegistrationByStudentId(id));
    }

    @Operation(
            summary = "Get course enrollments for a student",
            description = "Returns a list of all course enrollments for the student with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved course enrollments",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseEnrollmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}/course-enrollments")
    public ResponseEntity<List<DisplayCourseEnrollmentDto>> findCourseEnrollmentByStudentId(@PathVariable Long id) {
        return ResponseEntity.ok(courseEnrollmentApplicationService.findAllByStudentId(id));
    }

}
