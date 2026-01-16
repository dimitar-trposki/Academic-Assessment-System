package mk.ukim.finki.emc.academic_assessment_system_backend.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseEnrollmentApplicationService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/courses", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Course Enrollments",
        description = "Operations related to enrolling students in courses"
)
public class CourseEnrollmentController {

    private final CourseEnrollmentApplicationService courseEnrollmentApplicationService;
    private final CourseApplicationService courseApplicationService;

    @Operation(
            summary = "Export enrolled students for a course (CSV)",
            description = "Exports all students enrolled in the given course to a CSV file. " +
                    "Columns: studentIndex, firstName, lastName, major, email, academicRole."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV file successfully generated",
                    content = @Content(
                            mediaType = "text/csv"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found",
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}/export", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> exportEnrolledStudentsCsv(@PathVariable Long id) {
        byte[] csv = courseEnrollmentApplicationService.exportStudentsCsv(id);

        DisplayCourseDto courseDto = courseApplicationService.findById(id).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=course_" + courseDto.courseCode() + "_students.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(csv.length)
                .body(new ByteArrayResource(csv));
    }

    @Operation(
            summary = "Import enrolled students for a course (CSV)",
            description = "Imports and enrolls students into the given course from a CSV file. " +
                    "CSV must contain studentIndex in the first column (header optional)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Students successfully imported/enrolled",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid CSV / student index not found / invalid data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @PostMapping(value = "/{courseId}/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importEnrolledStudentsCsv(
            @PathVariable Long courseId,
            @Parameter(
                    description = "CSV file",
                    required = true,
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestParam("file") MultipartFile file
    ) {
        int imported = courseEnrollmentApplicationService.importStudentsCsv(courseId, file);
        return ResponseEntity.ok("Imported enrollments: " + imported);
    }

    @Operation(
            summary = "Get enrolled students for a course",
            description = "Returns a list of all enrolled students in the course with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved enrolled students",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseEnrollmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found",
                    content = @Content
            )
    })
    @GetMapping("/{courseId}/enrolled-students")
    public ResponseEntity<List<DisplayCourseEnrollmentDto>> getEnrolledStudents(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseEnrollmentApplicationService
                .findAllByCourseIdWithStudentAndUser(courseId));
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
    @DeleteMapping("/{courseId}/{id}/delete-enrollment")
    public ResponseEntity<DisplayCourseEnrollmentDto> deleteById(@PathVariable Long courseId, @PathVariable Long id) {
        return courseEnrollmentApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
