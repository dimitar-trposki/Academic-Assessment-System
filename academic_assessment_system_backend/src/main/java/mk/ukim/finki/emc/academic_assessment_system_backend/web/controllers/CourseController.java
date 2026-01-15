package mk.ukim.finki.emc.academic_assessment_system_backend.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseEnrollmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseStaffAssignmentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.security.JwtUserPrincipal;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseEnrollment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseEnrollmentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseStaffAssignmentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateCourseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayCourseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.CourseApplicationService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/courses", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Courses",
        description = "Operations related to courses (subjects)"
)
public class CourseController {

    private final CourseApplicationService courseApplicationService;
    private final CourseStaffAssignmentApplicationService courseStaffAssignmentApplicationService;
    private final CourseEnrollmentApplicationService courseEnrollmentApplicationService;
    private final StudentApplicationService studentApplicationService;

    @Operation(
            summary = "Get all courses",
            description = "Returns a list of all available courses."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of courses",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<DisplayCourseDto>> findAll() {
        return ResponseEntity.ok(
                courseApplicationService.findAll().stream()
                        .sorted(Comparator.comparing(DisplayCourseDto::academicYear).reversed()
                                .thenComparing(Comparator.comparing(DisplayCourseDto::semester).reversed()
                                        .thenComparing(DisplayCourseDto::courseCode)))
                        .toList()
        );
    }

    @Operation(
            summary = "Get course by ID",
            description = "Returns a course based on its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisplayCourseDto> findById(@PathVariable Long id) {
        return courseApplicationService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new course",
            description = "Creates a new course using the provided data."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayCourseDto> save(
            @Valid @RequestBody CreateCourseDto createCourseDto
    ) {
        return ResponseEntity.ok(courseApplicationService.save(createCourseDto));
    }

    @Operation(
            summary = "Update an existing course",
            description = "Updates the course with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisplayCourseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateCourseDto createCourseDto
    ) {
        return courseApplicationService
                .update(id, createCourseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a course",
            description = "Deletes the course with the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DisplayCourseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DisplayCourseDto> deleteById(@PathVariable Long id) {
        return courseApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find-by-staffId")
    public ResponseEntity<List<DisplayCourseDto>> findAllForStaff(Authentication authentication) {
        JwtUserPrincipal principal = (JwtUserPrincipal) authentication.getPrincipal();
        List<Long> courseIdsByStaffId = courseStaffAssignmentApplicationService
                .findCourseStaffAssignmentByUserId(principal.id())
                .stream().map(DisplayCourseStaffAssignmentDto::courseId)
                .toList();
        List<DisplayCourseDto> coursesByStaffId = courseIdsByStaffId.stream()
                .map(courseApplicationService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(DisplayCourseDto::academicYear).reversed()
                        .thenComparing(DisplayCourseDto::semester).reversed()
                        .thenComparing(DisplayCourseDto::courseCode))
                .toList();
        return ResponseEntity.ok(coursesByStaffId);
    }

    @GetMapping("/find-by-studentId")
    public ResponseEntity<List<DisplayCourseDto>> findAllForStudent(Authentication authentication) {
        JwtUserPrincipal principal = (JwtUserPrincipal) authentication.getPrincipal();
        Optional<DisplayStudentDto> studentDto = studentApplicationService.findStudentByUserId(principal.id());
        List<Long> courseIdsByStudentId = courseEnrollmentApplicationService
                .findAllByStudentId(studentDto.get().id())
                .stream().map(DisplayCourseEnrollmentDto::courseId)
                .toList();
        List<DisplayCourseDto> coursesByStudentId = courseIdsByStudentId.stream()
                .map(courseApplicationService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(DisplayCourseDto::academicYear).reversed()
                        .thenComparing(DisplayCourseDto::semester).reversed()
                        .thenComparing(DisplayCourseDto::courseCode))
                .toList();
        return ResponseEntity.ok(coursesByStudentId);
    }

}
