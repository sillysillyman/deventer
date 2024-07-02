package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.UserLoginType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndLoginType(String email, UserLoginType loginType);
}
