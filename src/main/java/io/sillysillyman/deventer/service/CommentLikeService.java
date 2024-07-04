package io.sillysillyman.deventer.service;

import io.sillysillyman.deventer.dto.comment.CommentResponseDto;
import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.entity.like.CommentLike;
import io.sillysillyman.deventer.repository.CommentLikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService extends LikeService<Comment, CommentLike, CommentResponseDto> {

    private final CommentLikeRepository commentLikeRepository;

    @Override
    protected Class<Comment> getEntityClass() {
        return Comment.class;
    }

    @Override
    protected Long getEntityOwnerId(Comment entity) {
        return entity.getUser().getId();
    }

    @Override
    protected Optional<CommentLike> findLikeByEntityAndUser(Comment entity, User user) {
        return commentLikeRepository.findByCommentAndUser(entity, user);
    }

    @Override
    protected CommentLike createLike(User user, Comment entity) {
        return new CommentLike(user, entity);
    }

    @Override
    protected String getEntityName() {
        return "댓글";
    }

    @Override
    protected JpaRepository<CommentLike, Long> getLikeRepository() {
        return commentLikeRepository;
    }

    @Override
    protected long countLikes(Long entityId) {
        return commentLikeRepository.countByCommentId(entityId);
    }

    @Override
    protected Page<CommentLike> getLikesByUser(User user, Pageable pageable) {
        return commentLikeRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    @Override
    protected CommentResponseDto convertToDto(CommentLike like) {
        return new CommentResponseDto(like.getComment());
    }
}