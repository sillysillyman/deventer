package io.sillysillyman.deventer.repository;


import io.sillysillyman.deventer.entity.Like;
import io.sillysillyman.deventer.enums.LikeableEntityType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByLikeableEntityIdAndLikeableEntityTypeAndUserId(Long likeableEntityId,
        LikeableEntityType likeableEntityType, Long userId);

    List<Like> findAllByLikeableEntityIdAndLikeableEntityType(Long likableEntityId,
        LikeableEntityType likeableEntityType);
}
