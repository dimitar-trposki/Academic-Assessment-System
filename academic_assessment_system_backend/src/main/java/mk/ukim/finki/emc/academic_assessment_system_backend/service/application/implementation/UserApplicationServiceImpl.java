package mk.ukim.finki.emc.academic_assessment_system_backend.service.application.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.LoginUserRequestDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.LoginUserResponseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.RegisterUserRequestDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.RegisterUserResponseDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.create.CreateUserDto;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.domain.display.*;
import mk.ukim.finki.emc.academic_assessment_system_backend.helpers.JwtHelper;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.Student;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.application.UserApplicationService;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.UserService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVRecord;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.*;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;

    public UserApplicationServiceImpl(UserService userService, JwtHelper jwtHelper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<DisplayUserDto> findAll() {
        return DisplayUserDto
                .from(userService.findAll());
    }

    @Override
    public List<DisplayUserDto> findAllStaff() {
        return DisplayUserDto.from(userService.findAllByUserRole(UserRole.STAFF));
    }

    @Override
    public List<DisplayUserStudentDto> findAllStudents() {
        return DisplayUserStudentDto.from(userService.findAllByUserRole(UserRole.STUDENT));
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
        User user = userService.findByEmail(loginUserRequestDto.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginUserRequestDto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtHelper.generateToken(user.getEmail());
        return Optional.of(new LoginUserResponseDto(token));

    }

    @Override
    public Optional<RegisterUserResponseDto> findByEmail(String email) {
        return userService
                .findByEmail(email)
                .map(RegisterUserResponseDto::from);
    }

    @Override
    public byte[] exportUsersToCsv() {
        List<User> users = userService.findAll();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .builder()
                     .setHeader("firstName", "lastName", "email", "password", "academicRole", "studentIndex", "major")
                     .build())
        ) {
            for (User u : users) {
                Student s = u.getStudent();
                String studentIndex = (s != null) ? s.getStudentIndex() : "";
                String major = (s != null) ? s.getMajor() : "";

                printer.printRecord(
                        u.getFirstName(),
                        u.getLastName(),
                        u.getEmail(),
                        "",
                        u.getUserRole().name(),
                        studentIndex,
                        major
                );
            }

            printer.flush();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to export users to CSV", e);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> importUsersFromCsv(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is required");
        }

        int created = 0;
        int updated = 0;

        List<Map<String, String>> errors = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .build()
                    .parse(reader);

            for (CSVRecord r : records) {
                try {
                    String firstName = r.get("firstName");
                    String lastName = r.get("lastName");
                    String email = r.get("email");
                    String password = r.get("password");
                    String roleStr = r.get("academicRole");
                    String studentIndex = getOptional(r, "studentIndex");
                    String major = getOptional(r, "major");

                    UserRole role = UserRole.valueOf(roleStr);

                    if (isBlank(firstName) || isBlank(lastName) || isBlank(email)) {
                        throw new IllegalArgumentException("firstName/lastName/email are required");
                    }

                    boolean isStudent = role == UserRole.STUDENT;

                    if (isStudent) {
                        if (isBlank(studentIndex) || isBlank(major)) {
                            throw new IllegalArgumentException("studentIndex and major are required for STUDENT");
                        }
                    }

                    Optional<User> existingOpt = userService.findByEmail(email);

                    if (existingOpt.isEmpty()) {
                        if (isBlank(password)) {
                            throw new IllegalArgumentException("password is required for new users");
                        }

                        User u = new User();
                        u.setFirstName(firstName);
                        u.setLastName(lastName);
                        u.setEmail(email);
                        u.setUserRole(role);

                        u.setPassword(passwordEncoder.encode(password));

                        if (isStudent) {
                            Student s = new Student(studentIndex, major, u);
                            u.setStudent(s);
                        } else {
                            u.setStudent(null);
                        }

                        userService.save(u);
                        created++;

                    } else {
                        User u = existingOpt.get();
                        u.setFirstName(firstName);
                        u.setLastName(lastName);
                        u.setUserRole(role);

                        if (!isBlank(password)) {
                            u.setPassword(passwordEncoder.encode(password));
                        }

                        if (isStudent) {
                            if (u.getStudent() == null) {
                                Student s = new Student(studentIndex, major, u);
                                u.setStudent(s);
                            } else {
                                u.getStudent().setStudentIndex(studentIndex);
                                u.getStudent().setMajor(major);
                            }
                        } else {
                            u.setStudent(null);
                        }

                        userService.save(u);
                        updated++;
                    }

                } catch (Exception rowEx) {
                    Map<String, String> err = new LinkedHashMap<>();
                    err.put("row", String.valueOf(r.getRecordNumber()));
                    err.put("email", safeGet(r, "email"));
                    err.put("error", rowEx.getMessage());
                    errors.add(err);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import users from CSV", e);
        }

        return Map.of(
                "created", created,
                "updated", updated,
                "errors", errors
        );
    }

    @Override
    public DisplayMyProfileDto myProfile(Long userId) {
        return null;
    }

    private static String getOptional(CSVRecord r, String col) {
        try {
            return r.isMapped(col) ? r.get(col) : "";
        } catch (Exception e) {
            return "";
        }
    }

    private static String safeGet(CSVRecord r, String col) {
        try {
            return r.isMapped(col) ? r.get(col) : "";
        } catch (Exception e) {
            return "";
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

}
