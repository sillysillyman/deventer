package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.like.CommentLike;
import io.sillysillyman.deventer.repository.query.CommentLikeRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>,
    JpaSpecificationExecutor<CommentLike>, CommentLikeRepositoryQuery {

}