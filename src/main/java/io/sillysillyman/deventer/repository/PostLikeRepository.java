package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.entity.like.PostLike;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    long countByPostId(Long postId);

    Optional<PostLike> findByPostAndUser(Post post, User user);

    Page<PostLike> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}