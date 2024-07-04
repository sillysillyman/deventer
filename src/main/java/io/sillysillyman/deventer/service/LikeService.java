package io.sillysillyman.deventer.service;

import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.UserActionError;
import io.sillysillyman.deventer.exception.UserActionNotAllowedException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class LikeService<T, L, D> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 엔티티에 좋아요를 토글합니다.
     *
     * @param entityId 엔티티 ID
     * @param user     현재 인증된 사용자 정보
     * @return 좋아요 처리 결과 메시지
     */
    @Transactional
    public String toggleLike(Long entityId, User user) {
        T entity = entityManager.find(getEntityClass(), entityId, LockModeType.PESSIMISTIC_WRITE);

        validateOwnership(getEntityOwnerId(entity), user.getId());

        Optional<L> optionalLike = findLikeByEntityAndUser(entity, user);
        if (optionalLike.isEmpty()) {
            L like = createLike(user, entity);
            getLikeRepository().save(like);
            return getEntityName() + "(Id: " + entityId + ")의 좋아요가 완료되었습니다.\n좋아요 개수: " + countLikes(
                entityId);
        } else {
            getLikeRepository().delete(optionalLike.get());
            return getEntityName() + "(Id: " + entityId + ")의 좋아요가 취소되었습니다.\n좋아요 개수: " + countLikes(
                entityId);
        }
    }

    /**
     * 사용자가 좋아요 한 엔티티 목록을 조회합니다.
     *
     * @param user     현재 인증된 사용자 정보
     * @param pageable 페이지 정보
     * @return 좋아요 한 엔티티 목록
     */
    @Transactional(readOnly = true)
    public Page<D> getLikedEntities(User user, Pageable pageable) {
        return getLikesByUser(user, pageable).map(this::convertToDto);
    }

    protected abstract Class<T> getEntityClass();

    protected abstract Long getEntityOwnerId(T entity);

    protected abstract Optional<L> findLikeByEntityAndUser(T entity, User user);

    protected abstract L createLike(User user, T entity);

    protected abstract String getEntityName();

    protected abstract JpaRepository<L, Long> getLikeRepository();

    protected abstract long countLikes(Long entityId);

    protected abstract Page<L> getLikesByUser(User user, Pageable pageable);

    protected abstract D convertToDto(L like);

    private void validateOwnership(Long entityOwnerId, Long userId) {
        if (entityOwnerId.equals(userId)) {
            throw new UserActionNotAllowedException(UserActionError.SELF_ACTION_NOT_ALLOWED);
        }
    }
}