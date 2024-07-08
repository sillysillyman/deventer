package io.sillysillyman.deventer.repository.query;

import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.entity.like.CommentLike;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentLikeRepositoryQuery {

    long countByCommentId(Long commentId);

    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    Page<CommentLike> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}