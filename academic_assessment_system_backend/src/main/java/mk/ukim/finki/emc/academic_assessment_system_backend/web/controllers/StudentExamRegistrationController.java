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
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayExamDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentExamRegistrationDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.security.JwtUserPrincipal;
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
        name = "Student Exam Registrations",
        description = "Operations related to student registrations for exams"
)
public class StudentExamRegistrationController {

    private final StudentExamRegistrationApplicationService studentExamRegistrationApplicationService;
    private final ExamApplicationService examApplicationService;

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
    @PostMapping("/{examId}/register")
    public ResponseEntity<DisplayStudentExamRegistrationDto> registerForExam(
            @PathVariable Long examId,
            Authentication authentication
    ) {
        JwtUserPrincipal principal = (JwtUserPrincipal) authentication.getPrincipal();
        String email = principal.email();

        return ResponseEntity.ok(
                studentExamRegistrationApplicationService
                        .registerCurrentStudent(email, examId)
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
    @GetMapping(value = "/{examId}/registered-students/export", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportRegisteredStudents(@PathVariable Long examId) {
        byte[] csv = studentExamRegistrationApplicationService.exportStudentsByExamStatusCsv(examId, ExamStatus.REGISTERED);
        DisplayExamDto exam = examApplicationService.findById(examId).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"exam_" + exam.course().courseCode() + "_registeredStudents.csv\"")
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
    @PostMapping(value = "/{examId}/attended-students/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importAttendedStudents(
            @PathVariable Long examId,
            @RequestPart("file") MultipartFile file
    ) {
        int importedLines = studentExamRegistrationApplicationService.importStudentsCsv(examId, file);
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
    @GetMapping(value = "/{examId}/attended-students/export", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportAttendedStudents(@PathVariable Long examId) {
        byte[] csv = studentExamRegistrationApplicationService.exportStudentsByExamStatusCsv(examId, ExamStatus.ATTENDED);
        DisplayExamDto exam = examApplicationService.findById(examId).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"exam_" + exam.course().courseCode() + "_attendedStudents.csv\"")
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
    @GetMapping(value = "/{examId}/absent-students/export", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportAbsentStudents(@PathVariable Long examId) {
        byte[] csv = studentExamRegistrationApplicationService.exportStudentsByExamStatusCsv(examId, ExamStatus.ABSENT);
        DisplayExamDto exam = examApplicationService.findById(examId).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"exam_" + exam.course().courseCode() + "_absentStudents.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new ByteArrayResource(csv));
    }

    @Operation(
            summary = "Get registered students for an exam",
            description = "Returns a list of all students registered for the exam with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved registered students",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @GetMapping("/{examId}/registered-students")
    public ResponseEntity<List<DisplayStudentExamRegistrationDto>> getRegisteredStudents(@PathVariable Long examId) {
        return ResponseEntity.ok(studentExamRegistrationApplicationService.findAllByExamIdAndExamStatus(examId, ExamStatus.REGISTERED));
    }

    @Operation(
            summary = "Get attended students for an exam",
            description = "Returns a list of all students who attended the exam with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved attended students",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @GetMapping("/{examId}/attended-students")
    public ResponseEntity<List<DisplayStudentExamRegistrationDto>> getAttendedStudents(@PathVariable Long examId) {
        return ResponseEntity.ok(studentExamRegistrationApplicationService.findAllByExamIdAndExamStatus(examId, ExamStatus.ATTENDED));
    }

    @Operation(
            summary = "Get absent students for an exam",
            description = "Returns a list of all students who were absent from the exam with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved absent students",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam not found",
                    content = @Content
            )
    })
    @GetMapping("/{examId}/absent-students")
    public ResponseEntity<List<DisplayStudentExamRegistrationDto>> getAbsentStudents(@PathVariable Long examId) {
        return ResponseEntity.ok(studentExamRegistrationApplicationService.findAllByExamIdAndExamStatus(examId, ExamStatus.ABSENT));
    }

//    @Operation(
//            summary = "Get all student exam registrations",
//            description = "Returns all student registrations for exams."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Successfully retrieved registrations",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
//                    )
//            )
//    })
//    @GetMapping
//    public ResponseEntity<List<DisplayStudentExamRegistrationDto>> findAll() {
//        return ResponseEntity.ok(studentExamRegistrationApplicationService.findAll());
//    }
//
//    @Operation(
//            summary = "Get student exam registration by ID",
//            description = "Returns a student exam registration based on its ID."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Registration found",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Registration not found",
//                    content = @Content
//            )
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<DisplayStudentExamRegistrationDto> findById(@PathVariable Long id) {
//        return studentExamRegistrationApplicationService
//                .findById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @Operation(
//            summary = "Register a student for an exam",
//            description = "Registers a student for an exam (initial status is REGISTERED)."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Student successfully registered for exam",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid input data",
//                    content = @Content
//            )
//    })
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<DisplayStudentExamRegistrationDto> save(
//            @Valid @RequestBody CreateStudentExamRegistrationDto createStudentExamRegistrationDto
//    ) {
//        return ResponseEntity.ok(
//                studentExamRegistrationApplicationService.save(createStudentExamRegistrationDto)
//        );
//    }
//
//    @Operation(
//            summary = "Update a student exam registration",
//            description = "Updates the exam registration with the given ID."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Registration successfully updated",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Registration not found",
//                    content = @Content
//            )
//    })
//    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<DisplayStudentExamRegistrationDto> update(
//            @PathVariable Long id,
//            @Valid @RequestBody CreateStudentExamRegistrationDto createStudentExamRegistrationDto
//    ) {
//        return studentExamRegistrationApplicationService
//                .update(id, createStudentExamRegistrationDto)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

//    @Operation(
//            summary = "Delete a student exam registration",
//            description = "Deletes the student exam registration with the given ID."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Registration successfully deleted",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = DisplayStudentExamRegistrationDto.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Registration not found",
//                    content = @Content
//            )
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<DisplayStudentExamRegistrationDto> deleteById(@PathVariable Long id) {
//        return studentExamRegistrationApplicationService
//                .deleteById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
}
