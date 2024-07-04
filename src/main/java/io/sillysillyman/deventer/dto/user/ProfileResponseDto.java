package io.sillysillyman.deventer.dto.user;

import io.sillysillyman.deventer.entity.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private final Long userId;
    private final String username;
    private final String nickname;
    private final long postLikeCount;
    private final long commentLikeCount;
    private final String email;

    public ProfileResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.postLikeCount = user.getPostLikes().size();
        this.commentLikeCount = user.getCommentLikes().size();
        this.email = user.getEmail();
    }
}