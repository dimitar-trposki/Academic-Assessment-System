package mk.ukim.finki.emc.academic_assessment_system_backend.service.domain;

import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    Optional<User> update(Long id, User user);

    Optional<User> deleteById(Long id);

    User register(User user);

    User login(String email, String password);

    Optional<User> findByEmail(String email);

}
