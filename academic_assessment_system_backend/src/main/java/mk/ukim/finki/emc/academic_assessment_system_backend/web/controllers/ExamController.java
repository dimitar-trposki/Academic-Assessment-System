package mk.ukim.finki.emc.academic_assessment_system_backend.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateExamDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayExamDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.security.JwtUserPrincipal;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Exam;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.ExamStatus;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.ExamApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentExamRegistrationApplicationService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/exams",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Exams",
        description = "Operations related to exam sessions and exam scheduling"
)
public class ExamController {

    private final ExamApplicationService examApplicationService;
    private final StudentExamRegistrationApplicationService studentExamRegistrationApplicationService;

    @Operation(
            summary = "Get all exams",
            description = "Returns a list of all exams."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of exams",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayExamDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<DisplayExamDto>> findAll() {
        return ResponseEntity.ok(examApplicationService.findAll());
    }

    @Operation(
            summary = "Get exam by ID",
            description = "Returns an exam based on its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Exam found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayExamDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisplayExamDto> findById(@PathVariable Long id) {
        return examApplicationService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new exam",
            description = "Creates a new exam session with date, time, capacity, and reserved laboratories."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Exam successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayExamDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayExamDto> save(
            @Valid @RequestBody CreateExamDto createExamDto
    ) {
        return ResponseEntity.ok(examApplicationService.save(createExamDto));
    }

    @Operation(
            summary = "Update an existing exam",
            description = "Updates the exam with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Exam successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayExamDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayExamDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateExamDto createExamDto
    ) {
        return examApplicationService
                .update(id, createExamDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete an exam",
            description = "Deletes the exam with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Exam successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayExamDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayExamDto> deleteById(@PathVariable Long id) {
        return examApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Register logged-in student for exam",
            description = "Registers the currently authenticated STUDENT for the specified exam."
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
                    description = "Student already registered or invalid request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User is not a STUDENT",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{id}/register")
    public ResponseEntity<DisplayStudentExamRegistrationDto> registerForExam(
            @PathVariable Long id,
            Authentication authentication
    ) {
        JwtUserPrincipal principal = (JwtUserPrincipal) authentication.getPrincipal();
        String email = principal.email();

        return ResponseEntity.ok(
                studentExamRegistrationApplicationService
                        .registerCurrentStudent(email, id)
        );
    }

    @Operation(
            summary = "Export registered students for exam (CSV)",
            description = "Exports a CSV file containing all students registered for the given exam."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV file successfully generated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}/registered-students/export", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportRegisteredStudents(@PathVariable Long id) {
        byte[] csv = studentExamRegistrationApplicationService.exportStudentsByExamStatusCsv(id, ExamStatus.REGISTERED);
        DisplayExamDto exam = examApplicationService.findById(id).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"exam_" + exam.course().getCourseCode() + "_registeredStudents.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new ByteArrayResource(csv));
    }

    @Operation(
            summary = "Import attended students for exam",
            description = "Imports a CSV file containing students who attended the exam and updates their status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Students successfully imported"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid CSV file or format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @PostMapping(value = "/{id}/attended-students/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importAttendedStudents(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) {
        int importedLines = studentExamRegistrationApplicationService.importStudentsCsv(id, file);
        return ResponseEntity.ok("Imported students who attended the exam: " + importedLines);
    }

    @Operation(
            summary = "Export attended students for exam (CSV)",
            description = "Exports a CSV file containing all students who attended the given exam."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV file successfully generated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}/attended-students/export", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportAttendedStudents(@PathVariable Long id) {
        byte[] csv = studentExamRegistrationApplicationService.exportStudentsByExamStatusCsv(id, ExamStatus.ATTENDED);
        DisplayExamDto exam = examApplicationService.findById(id).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"exam_" + exam.course().getCourseCode() + "_attendedStudents.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new ByteArrayResource(csv));
    }

    @Operation(
            summary = "Export absent students for exam (CSV)",
            description = "Exports a CSV file containing all students who were absent from the given exam."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV file successfully generated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}/absent-students/export", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportAbsentStudents(@PathVariable Long id) {
        byte[] csv = studentExamRegistrationApplicationService.exportStudentsByExamStatusCsv(id, ExamStatus.ABSENT);
        DisplayExamDto exam = examApplicationService.findById(id).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"exam_" + exam.course().getCourseCode() + "_absentStudents.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new ByteArrayResource(csv));
    }

}
