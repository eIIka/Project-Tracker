package ua.ellka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ellka.model.user.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

}
