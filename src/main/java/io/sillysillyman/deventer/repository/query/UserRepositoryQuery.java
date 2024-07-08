package io.sillysillyman.deventer.repository.query;

import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.UserLoginType;
import java.util.Optional;

public interface UserRepositoryQuery {

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailAndLoginType(String email, UserLoginType loginType);
}