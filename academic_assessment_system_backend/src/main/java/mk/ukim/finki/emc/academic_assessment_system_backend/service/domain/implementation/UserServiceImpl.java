package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.implementation;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.enums.UserRole;
import mk.ukim.finki.emc.academic_assessment_system_backend.repository.UserRepository;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllByUserRole(UserRole userRole) {
        return userRepository.findAllByUserRole(userRole);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> update(Long id, User user) {
        return findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    existingUser.setUserRole(user.getUserRole());
                    existingUser.setStudent(user.getStudent());
                    return userRepository.save(existingUser);
                });
    }

    @Override
    public Optional<User> deleteById(Long id) {
        Optional<User> user = findById(id);
        user.ifPresent(userRepository::delete);
        return user;
    }

    @Override
    public User register(User user) {
//        if (findByUsername(user.getUsername()).isPresent())
//            throw new UsernameAlreadyExistsException(user.getUsername());

        return userRepository.save(new User(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                user.getUserRole()
        ));
    }

    @Override
    public User login(String email, String password) {
        User user = findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
//        if (!passwordEncoder.matches(password, user.getPassword()))
//            throw new IncorrectPasswordException();
        return user;
    }

    @Override
    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository
//                .findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException(username));
//    }
}
