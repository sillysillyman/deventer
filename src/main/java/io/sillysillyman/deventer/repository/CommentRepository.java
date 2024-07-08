package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.repository.query.CommentRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository extends JpaRepository<Comment, Long>,
    JpaSpecificationExecutor<Comment>, CommentRepositoryQuery {

}