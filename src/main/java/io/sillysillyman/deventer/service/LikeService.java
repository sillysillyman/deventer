package io.sillysillyman.deventer.service;

import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.entity.Like;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.LikeableEntityType;
import io.sillysillyman.deventer.enums.NotFoundEntity;
import io.sillysillyman.deventer.enums.UserActionError;
import io.sillysillyman.deventer.exception.EntityNotFoundException;
import io.sillysillyman.deventer.exception.UserActionNotAllowedException;
import io.sillysillyman.deventer.repository.CommentRepository;
import io.sillysillyman.deventer.repository.LikeRepository;
import io.sillysillyman.deventer.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 좋아요를 처리합니다. 이미 좋아요가 되어있으면 취소하고, 그렇지 않으면 좋아요를 추가합니다.
     *
     * @param likeableEntityId   좋아요를 할 엔티티의 ID
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     * @param user               현재 인증된 사용자 정보
     * @return 좋아요가 추가되었으면 true, 취소되었으면 false
     */
    @Transactional
    public boolean toggleLike(Long likeableEntityId, String likeableEntityType, User user) {
        LikeableEntityType entityType = LikeableEntityType.getByType(likeableEntityType);

        lockEntity(likeableEntityId, entityType);

        Optional<Like> optionalLike = likeRepository.findByLikeableEntityIdAndLikeableEntityTypeAndUserId(
            likeableEntityId, entityType, user.getId());

        if (optionalLike.isEmpty()) {
            validateLikeableEntityOwnership(likeableEntityId, entityType, user);
            Like like = new Like(likeableEntityId, likeableEntityType, user);
            likeRepository.save(like);
            return true;
        } else {
            validateLikeableEntityOwnership(likeableEntityId, entityType, user);
            likeRepository.delete(optionalLike.get());
            return false;
        }
    }

    /**
     * 특정 엔티티에 대한 좋아요 개수를 반환합니다.
     *
     * @param likeableEntityId   좋아요를 할 엔티티의 ID
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     * @return 좋아요 개수
     */
    public int likeCount(Long likeableEntityId, String likeableEntityType) {
        return likeRepository.findAllByLikeableEntityIdAndLikeableEntityType(likeableEntityId,
            LikeableEntityType.getByType(likeableEntityType)).size();
    }

    /**
     * 특정 엔티티의 소유권을 검증합니다. 사용자가 자신의 엔티티에 좋아요를 할 수 없도록 합니다.
     *
     * @param likeableEntityId   좋아요를 할 엔티티의 ID
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     * @param user               현재 인증된 사용자
     */
    private void validateLikeableEntityOwnership(
        Long likeableEntityId,
        LikeableEntityType likeableEntityType,
        User user) {

        if (likeableEntityType == LikeableEntityType.POST) {
            Post post = postRepository.findById(likeableEntityId).orElseThrow(
                () -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
            validateOwnership(post.getUser().getId(), user.getId());
        } else if (likeableEntityType == LikeableEntityType.COMMENT) {
            Comment comment = commentRepository.findById(likeableEntityId).orElseThrow(
                () -> new EntityNotFoundException(NotFoundEntity.COMMENT_NOT_FOUND));
            validateOwnership(comment.getUser().getId(), user.getId());
        } else {
            throw new IllegalArgumentException("좋아요를 할 수 없는 엔티티 타입입니다.");
        }
    }

    /**
     * 특정 엔티티에 대해 락을 설정합니다.
     *
     * @param likeableEntityId   좋아요를 할 엔티티의 ID
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     */
    private void lockEntity(Long likeableEntityId, LikeableEntityType likeableEntityType) {
        if (likeableEntityType == LikeableEntityType.POST) {
            entityManager.find(Post.class, likeableEntityId, LockModeType.PESSIMISTIC_WRITE);
        } else if (likeableEntityType == LikeableEntityType.COMMENT) {
            entityManager.find(Comment.class, likeableEntityId, LockModeType.PESSIMISTIC_WRITE);
        }
    }

    /**
     * 소유권을 검증합니다. 사용자가 자신의 엔티티에 대해 작업을 수행할 수 없도록 합니다.
     *
     * @param entityOwnerId 엔티티 소유자의 ID
     * @param userId        현재 인증된 사용자 ID
     */
    private void validateOwnership(Long entityOwnerId, Long userId) {
        if (entityOwnerId.equals(userId)) {
            throw new UserActionNotAllowedException(UserActionError.SELF_ACTION_NOT_ALLOWED);
        }
    }
}

