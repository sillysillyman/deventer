package io.sillysillyman.deventer.dto.user;

import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.UserRole;
import io.sillysillyman.deventer.enums.UserStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long userId;
    private final String username;
    private final String nickname;
    private final String email;
    private final UserRole role;
    private final UserStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}