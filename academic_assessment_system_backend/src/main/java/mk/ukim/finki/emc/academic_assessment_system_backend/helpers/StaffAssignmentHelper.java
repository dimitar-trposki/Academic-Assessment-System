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

    /**
     * Completely updates (replaces) staff assignments for the course.
     * Existing assignments се реупотребуваат ако и понатаму треба да постојат,
     * останатите се бришат, а новите се креираат.
     */
    public void applyStaffAssignments(Course course,
                                      List<Long> professorIds,
                                      List<Long> assistantIds) {

        // 0) Normalizacija на листите
        List<Long> profIds = professorIds == null
                ? List.of()
                : professorIds.stream().distinct().toList();

        List<Long> asstIds = assistantIds == null
                ? List.of()
                : assistantIds.stream().distinct().toList();

        // 1) Сите userId + улога што треба да постојат после UPDATE
        Set<String> desiredKeys = new HashSet<>();
        for (Long id : profIds) {
            desiredKeys.add(buildKey(id, StaffRole.PROFESSOR));
        }
        for (Long id : asstIds) {
            desiredKeys.add(buildKey(id, StaffRole.ASSISTANT));
        }

        // 2) Ги вчитуваме сите потребни users со едно query
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

        // 3) Постоечки assignments на курсот
        List<CourseStaffAssignment> currentAssignments =
                new ArrayList<>(course.getCourseStaffAssignments());

        // set од keys што реално постојат во базата
        Set<String> existingKeys = new HashSet<>();

        // 3a) Проаѓаме низ сите моментални assignments
        //     ако повеќе не се во desired -> ги бришеме
        //     ако се во desired -> ги оставаме и ги вадиме од desired (за да не ги креираме втор пат)
        for (CourseStaffAssignment csa : currentAssignments) {
            Long uid = csa.getUser().getId();
            StaffRole role = csa.getStaffRole();
            String key = buildKey(uid, role);
            existingKeys.add(key);

            if (!desiredKeys.contains(key)) {
                // Овој assignment веќе не е потребен -> delete
                courseStaffAssignmentRepository.delete(csa);
                course.getCourseStaffAssignments().remove(csa);
            } else {
                // Овој assignment треба да остане -> само го вадиме од desired
                desiredKeys.remove(key);
            }
        }

        // 4) Сè што остана во desiredKeys се assignments што НЕ постојат и треба да ги креираме
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
