package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.repository.query.PostRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>,
    PostRepositoryQuery {

}