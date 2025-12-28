package mk.ukim.finki.emc.academic_assessment_system_backend.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentExamRegistrationApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/student-exam-registrations",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Student Exam Registrations",
        description = "Operations related to student registrations for exams"
)
public class StudentExamRegistrationController {

    private final StudentExamRegistrationApplicationService studentExamRegistrationApplicationService;

    @Operation(
            summary = "Get all student exam registrations",
            description = "Returns all student registrations for exams."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved registrations",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<DisplayStudentExamRegistrationDto>> findAll() {
        return ResponseEntity.ok(studentExamRegistrationApplicationService.findAll());
    }

    @Operation(
            summary = "Get student exam registration by ID",
            description = "Returns a student exam registration based on its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registration not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisplayStudentExamRegistrationDto> findById(@PathVariable Long id) {
        return studentExamRegistrationApplicationService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Register a student for an exam",
            description = "Registers a student for an exam (initial status is REGISTERED)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student successfully registered for exam",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayStudentExamRegistrationDto> save(
            @Valid @RequestBody CreateStudentExamRegistrationDto createStudentExamRegistrationDto
    ) {
        return ResponseEntity.ok(
                studentExamRegistrationApplicationService.save(createStudentExamRegistrationDto)
        );
    }

    @Operation(
            summary = "Update a student exam registration",
            description = "Updates the exam registration with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registration not found",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayStudentExamRegistrationDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateStudentExamRegistrationDto createStudentExamRegistrationDto
    ) {
        return studentExamRegistrationApplicationService
                .update(id, createStudentExamRegistrationDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a student exam registration",
            description = "Deletes the student exam registration with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registration not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayStudentExamRegistrationDto> deleteById(@PathVariable Long id) {
        return studentExamRegistrationApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
