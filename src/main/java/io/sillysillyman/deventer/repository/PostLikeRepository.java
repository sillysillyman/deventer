package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.like.PostLike;
import io.sillysillyman.deventer.repository.query.PostLikeRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>,
    JpaSpecificationExecutor<PostLike>, PostLikeRepositoryQuery {

}