package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
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
    public DisplayStudentDto save(CreateStudentDto createStudentDto) {
        Optional<User> user = userService.findById(createStudentDto.userId());

        return DisplayStudentDto
                .from(studentService.save(createStudentDto.toStudent(user.get())));
    }

    @Override
    public Optional<DisplayStudentDto> update(Long id, CreateStudentDto createStudentDto) {
        Optional<User> user = userService.findById(createStudentDto.userId());

        return studentService
                .update(id, createStudentDto.toStudent(user.get()))
                .map(DisplayStudentDto::from);
    }

    @Override
    public Optional<DisplayStudentDto> deleteById(Long id) {
        return studentService
                .deleteById(id)
                .map(DisplayStudentDto::from);
    }
}
