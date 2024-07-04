package io.sillysillyman.deventer.dto.comment;


import io.sillysillyman.deventer.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final String content;
    private final Long userId;
    private final String nickname;
    private final int likeCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.likeCount = comment.getLikes().size();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}