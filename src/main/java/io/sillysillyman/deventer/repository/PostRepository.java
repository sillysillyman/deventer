package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Category;
import io.sillysillyman.deventer.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findAllByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);
}
