package mk.ukim.finki.emc.academic_assessment_system_backend.service.application;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.LoginUserRequestDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.LoginUserResponseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.RegisterUserRequestDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.RegisterUserResponseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateUserDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayMyProfileDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayStudentDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayUserDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayUserStudentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserApplicationService {

    List<DisplayUserDto> findAll();

    List<DisplayUserDto> findAllStaff();

    List<DisplayUserStudentDto> findAllStudents();

    Optional<DisplayUserDto> findById(Long id);

    DisplayUserDto save(CreateUserDto createUserDto);

    Optional<DisplayUserDto> update(Long id, CreateUserDto createUserDto);

    Optional<DisplayUserDto> deleteById(Long id);

    Optional<RegisterUserResponseDto> register(RegisterUserRequestDto registerUserRequestDto);

    Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto);

    Optional<RegisterUserResponseDto> findByEmail(String email);

    byte[] exportUsersToCsv();

    Object importUsersFromCsv(MultipartFile file);

    DisplayMyProfileDto myProfile(Long userId);

}
