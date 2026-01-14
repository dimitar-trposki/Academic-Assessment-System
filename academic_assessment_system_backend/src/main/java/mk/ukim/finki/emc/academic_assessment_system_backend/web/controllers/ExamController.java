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
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.ExamApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentExamRegistrationApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @PutMapping(value = "/{id}/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DisplayExamDto> deleteById(@PathVariable Long id) {
        return examApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
