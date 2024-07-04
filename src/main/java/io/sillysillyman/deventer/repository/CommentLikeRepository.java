package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.entity.like.CommentLike;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    long countByCommentId(Long commentId);

    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    Page<CommentLike> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}