package io.sillysillyman.deventer.service;

import io.sillysillyman.deventer.dto.post.PostResponseDto;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.entity.like.PostLike;
import io.sillysillyman.deventer.repository.PostLikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService extends LikeService<Post, PostLike, PostResponseDto> {

    private final PostLikeRepository postLikeRepository;

    @Override
    protected Class<Post> getEntityClass() {
        return Post.class;
    }

    @Override
    protected Long getEntityOwnerId(Post post) {
        return post.getUser().getId();
    }

    @Override
    protected Optional<PostLike> findLikeByEntityAndUser(Post post, User user) {
        return postLikeRepository.findByPostAndUser(post, user);
    }

    @Override
    protected PostLike createLike(User user, Post post) {
        return new PostLike(user, post);
    }

    @Override
    protected String getEntityName() {
        return "게시물";
    }

    @Override
    protected JpaRepository<PostLike, Long> getLikeRepository() {
        return postLikeRepository;
    }

    @Override
    protected long countLikes(Long entityId) {
        return postLikeRepository.countByPostId(entityId);
    }

    @Override
    protected Page<PostLike> getLikesByUser(User user, Pageable pageable) {
        return postLikeRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    @Override
    protected PostResponseDto convertToDto(PostLike like) {
        return new PostResponseDto(like.getPost());
    }
}