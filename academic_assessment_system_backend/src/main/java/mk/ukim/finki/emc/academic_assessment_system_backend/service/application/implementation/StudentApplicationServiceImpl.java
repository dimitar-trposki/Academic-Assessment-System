package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.StudentApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.StudentService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentApplicationServiceImpl implements StudentApplicationService {

    private final StudentService studentService;
    private final UserService userService;

    public StudentApplicationServiceImpl(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    @Override
    public List<DisplayStudentDto> findAll() {
        return DisplayStudentDto
                .from(studentService.findAll());
    }

    @Override
    public Optional<DisplayStudentDto> findById(Long id) {
        return studentService
                .findById(id)
                .map(DisplayStudentDto::from);
    }

    @Override
    public Optional<DisplayStudentDto> save(CreateStudentDto createStudentDto) {
        Optional<User> user = userService.findById(createStudentDto.userId());

        if (user.isPresent() && user.get().getUserRole().equals(UserRole.STUDENT)) {
            return Optional.of(DisplayStudentDto
                    .from(studentService.save(createStudentDto.toStudent(user.get()))));
        }

        return Optional.empty();
    }

    @Override
    public Optional<DisplayStudentDto> update(Long id, CreateStudentDto createStudentDto) {
        Optional<User> user = userService.findById(createStudentDto.userId());

        return studentService
                .update(id, createStudentDto.toStudent(user.get()))
                .map(DisplayStudentDto::from);
    }

    @Override
    public Optional<DisplayStudentDto> deleteByIdWithUser(Long id) {
        return studentService
                .deleteByIdWithUser(id)
                .map(DisplayStudentDto::from);
    }

    @Override
    public Optional<DisplayStudentDto> deleteByIdWithoutUser(Long id) {
        return studentService
                .deleteByIdWithoutUser(id)
                .map(DisplayStudentDto::from);
    }

    @Override
    public Optional<DisplayStudentDto> findByStudentIndex(String studentIndex) {
        return studentService
                .findByStudentIndex(studentIndex)
                .map(DisplayStudentDto::from);
    }

    @Override
    public Optional<DisplayStudentDto> findStudentByUserId(Long userId) {
        return studentService
                .findStudentByUserId(userId)
                .map(DisplayStudentDto::from);
    }

}
