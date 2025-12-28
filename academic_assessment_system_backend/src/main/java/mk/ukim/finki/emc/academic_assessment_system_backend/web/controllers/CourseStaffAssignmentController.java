package mk.ukim.finki.emc.academic_assessment_system_backend.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseStaffAssignmentApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/course-staff-assignments",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Course Staff Assignments",
        description = "Operations related to assigning professors and assistants to courses"
)
public class CourseStaffAssignmentController {

    private final CourseStaffAssignmentApplicationService courseStaffAssignmentApplicationService;

    @Operation(
            summary = "Get all course staff assignments",
            description = "Returns all staff assignments for all courses."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved assignments",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseStaffAssignmentDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<DisplayCourseStaffAssignmentDto>> findAll() {
        return ResponseEntity.ok(courseStaffAssignmentApplicationService.findAll());
    }

    @Operation(
            summary = "Get course staff assignment by ID",
            description = "Returns a staff assignment based on its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Assignment found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseStaffAssignmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Assignment not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisplayCourseStaffAssignmentDto> findById(@PathVariable Long id) {
        return courseStaffAssignmentApplicationService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a course staff assignment",
            description = "Assigns a professor or assistant to a course."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Assignment successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseStaffAssignmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayCourseStaffAssignmentDto> save(
            @Valid @RequestBody CreateCourseStaffAssignmentDto createCourseStaffAssignmentDto
    ) {
        return ResponseEntity.ok(
                courseStaffAssignmentApplicationService.save(createCourseStaffAssignmentDto)
        );
    }

    @Operation(
            summary = "Update a course staff assignment",
            description = "Updates the staff assignment with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Assignment successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseStaffAssignmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Assignment not found",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayCourseStaffAssignmentDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateCourseStaffAssignmentDto createCourseStaffAssignmentDto
    ) {
        return courseStaffAssignmentApplicationService
                .update(id, createCourseStaffAssignmentDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a course staff assignment",
            description = "Deletes the staff assignment with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Assignment successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseStaffAssignmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Assignment not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayCourseStaffAssignmentDto> deleteById(@PathVariable Long id) {
        return courseStaffAssignmentApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
