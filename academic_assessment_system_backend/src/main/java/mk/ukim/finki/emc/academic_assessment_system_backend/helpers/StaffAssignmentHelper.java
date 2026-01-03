package mk.ukim.finki.emc.academic_assessment_system_backend.helpers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.StaffRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.CourseStaffAssignmentRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class StaffAssignmentHelper {

    private final UserRepository userRepository;
    private final CourseStaffAssignmentRepository courseStaffAssignmentRepository;

    public void applyStaffAssignments(Course course, List<Long> professorIds, List<Long> assistantIds) {

        List<Long> profIds = professorIds == null ? List.of() : professorIds;
        List<Long> asstIds = assistantIds == null ? List.of() : assistantIds;

        List<Long> allIds = Stream.concat(profIds.stream(), asstIds.stream())
                .distinct()
                .toList();

        Map<Long, User> usersById = userRepository.findAllById(allIds)
                .stream()
                .collect(java.util.stream.Collectors.toMap(User::getId, u -> u));

        for (Long uid : allIds) {
            if (!usersById.containsKey(uid)) {
                throw new RuntimeException("User not found: " + uid);
            }
        }

        for (Long uid : profIds.stream().distinct().toList()) {
            User user = usersById.get(uid);
            CourseStaffAssignment saved = courseStaffAssignmentRepository.save(
                    new CourseStaffAssignment(user, course, StaffRole.PROFESSOR)
            );
            course.getCourseStaffAssignments().add(saved);
        }

        for (Long uid : asstIds.stream().distinct().toList()) {
            User user = usersById.get(uid);
            CourseStaffAssignment saved = courseStaffAssignmentRepository.save(
                    new CourseStaffAssignment(user, course, StaffRole.ASSISTANT)
            );
            course.getCourseStaffAssignments().add(saved);
        }
    }
}
