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
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseStaffAssignmentApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/courses",
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
            summary = "Get assigned staff for a course",
            description = "Returns all staff members assigned to the course (professors, assistants)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved assigned staff list",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseStaffAssignmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found",
                    content = @Content
            )
    })
    @GetMapping("/{courseId}/assigned-staff")
    public ResponseEntity<List<DisplayCourseStaffAssignmentDto>> getCourseAssignedStaff(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseStaffAssignmentApplicationService
                .findAllByCourseIdWithUser(courseId));
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
    @DeleteMapping("/{courseId}/{id}/delete-assignment/")
    public ResponseEntity<DisplayCourseStaffAssignmentDto> deleteById(@PathVariable Long courseId, @PathVariable Long id) {
        return courseStaffAssignmentApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
