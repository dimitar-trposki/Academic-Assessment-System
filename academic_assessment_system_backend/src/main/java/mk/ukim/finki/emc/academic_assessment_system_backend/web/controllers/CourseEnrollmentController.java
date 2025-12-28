package mk.ukim.finki.emc.academic_assessment_system_backend.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseEnrollmentApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/course-enrollments", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Course Enrollments",
        description = "Operations related to enrolling students in courses"
)
public class CourseEnrollmentController {

    private final CourseEnrollmentApplicationService courseEnrollmentApplicationService;

    @Operation(
            summary = "Get all course enrollments",
            description = "Returns a list of all course enrollments (student-course relationships)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of enrollments",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseEnrollmentDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<DisplayCourseEnrollmentDto>> findAll() {
        return ResponseEntity.ok(courseEnrollmentApplicationService.findAll());
    }

    @Operation(
            summary = "Get course enrollment by ID",
            description = "Returns a course enrollment based on its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Enrollment found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseEnrollmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Enrollment not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisplayCourseEnrollmentDto> findById(@PathVariable Long id) {
        return courseEnrollmentApplicationService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a course enrollment",
            description = "Enrolls a student into a course using studentId and courseId."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Enrollment successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseEnrollmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayCourseEnrollmentDto> save(
            @Valid @RequestBody CreateCourseEnrollmentDto createCourseEnrollmentDto
    ) {
        return ResponseEntity.ok(courseEnrollmentApplicationService.save(createCourseEnrollmentDto));
    }

    @Operation(
            summary = "Update a course enrollment",
            description = "Updates the enrollment with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Enrollment successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseEnrollmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Enrollment not found",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayCourseEnrollmentDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateCourseEnrollmentDto createCourseEnrollmentDto
    ) {
        return courseEnrollmentApplicationService
                .update(id, createCourseEnrollmentDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a course enrollment",
            description = "Deletes the enrollment with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Enrollment successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseEnrollmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Enrollment not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayCourseEnrollmentDto> deleteById(@PathVariable Long id) {
        return courseEnrollmentApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
