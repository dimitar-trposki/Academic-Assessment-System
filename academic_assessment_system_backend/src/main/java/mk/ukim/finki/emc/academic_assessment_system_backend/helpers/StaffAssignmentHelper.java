package mk.ukim.finki.emc.academic_assessment_system_backend.helpers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Course;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.CourseStaffAssignment;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.StaffRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.CourseStaffAssignmentRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class StaffAssignmentHelper {

    private final UserRepository userRepository;
    private final CourseStaffAssignmentRepository courseStaffAssignmentRepository;

    public void applyStaffAssignments(Course course,
                                      List<Long> professorIds,
                                      List<Long> assistantIds) {

        List<Long> profIds = professorIds == null
                ? List.of()
                : professorIds.stream().distinct().toList();

        List<Long> asstIds = assistantIds == null
                ? List.of()
                : assistantIds.stream().distinct().toList();

        Set<String> desiredKeys = new HashSet<>();
        for (Long id : profIds) {
            desiredKeys.add(buildKey(id, StaffRole.PROFESSOR));
        }
        for (Long id : asstIds) {
            desiredKeys.add(buildKey(id, StaffRole.ASSISTANT));
        }

        Set<Long> allIds = new HashSet<>();
        allIds.addAll(profIds);
        allIds.addAll(asstIds);

        Map<Long, User> usersById = userRepository.findAllById(allIds)
                .stream()
                .collect(java.util.stream.Collectors.toMap(User::getId, u -> u));

        for (Long uid : allIds) {
            if (!usersById.containsKey(uid)) {
                throw new RuntimeException("User not found: " + uid);
            }
        }

        List<CourseStaffAssignment> currentAssignments =
                new ArrayList<>(course.getCourseStaffAssignments());

        Set<String> existingKeys = new HashSet<>();

        for (CourseStaffAssignment csa : currentAssignments) {
            Long uid = csa.getUser().getId();
            StaffRole role = csa.getStaffRole();
            String key = buildKey(uid, role);
            existingKeys.add(key);

            if (!desiredKeys.contains(key)) {
                courseStaffAssignmentRepository.delete(csa);
                course.getCourseStaffAssignments().remove(csa);
            } else {
                desiredKeys.remove(key);
            }
        }

        for (String key : desiredKeys) {
            String[] parts = key.split("_");
            Long userId = Long.parseLong(parts[0]);
            StaffRole role = StaffRole.valueOf(parts[1]);

            User user = usersById.get(userId);

            CourseStaffAssignment saved = courseStaffAssignmentRepository.save(
                    new CourseStaffAssignment(user, course, role)
            );
            course.getCourseStaffAssignments().add(saved);
        }
    }

    private String buildKey(Long userId, StaffRole role) {
        return userId + "_" + role.name();
    }
}
