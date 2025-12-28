package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.LoginUserRequestDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.LoginUserResponseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.RegisterUserRequestDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.RegisterUserResponseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateUserDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.DisplayUserDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.helpers.JwtHelper;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.UserApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserService userService;
    private final JwtHelper jwtHelper;

    public UserApplicationServiceImpl(UserService userService, JwtHelper jwtHelper) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public List<DisplayUserDto> findAll() {
        return DisplayUserDto
                .from(userService.findAll());
    }

    @Override
    public Optional<DisplayUserDto> findById(Long id) {
        return userService
                .findById(id)
                .map(DisplayUserDto::from);
    }

    @Override
    public DisplayUserDto save(CreateUserDto createUserDto) {
        return DisplayUserDto
                .from(userService.save(createUserDto.toUser()));
    }

    @Override
    public Optional<DisplayUserDto> update(Long id, CreateUserDto createUserDto) {
        return userService
                .update(id, createUserDto.toUser())
                .map(DisplayUserDto::from);
    }

    @Override
    public Optional<DisplayUserDto> deleteById(Long id) {
        return userService
                .deleteById(id)
                .map(DisplayUserDto::from);
    }

    @Override
    public Optional<RegisterUserResponseDto> register(RegisterUserRequestDto registerUserRequestDto) {
        User user = userService.register(registerUserRequestDto.toUser());
        RegisterUserResponseDto displayUserDto = RegisterUserResponseDto.from(user);
        return Optional.of(displayUserDto);
    }

    @Override
    public Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto) {
//        User user = userService.login(loginUserRequestDto.email(), loginUserRequestDto.password());
//
//        String token = jwtHelper.generateToken((UserDetails) user);
//
//        return Optional.of(new LoginUserResponseDto(token));
        User user = userService.findByEmail(loginUserRequestDto.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        String token = jwtHelper.generateToken(user.getEmail());

        return Optional.of(new LoginUserResponseDto(token));

    }

    @Override
    public Optional<RegisterUserResponseDto> findByEmail(String email) {
        return userService
                .findByEmail(email)
                .map(RegisterUserResponseDto::from);
    }
}
