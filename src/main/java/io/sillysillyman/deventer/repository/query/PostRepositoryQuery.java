package io.sillysillyman.deventer.repository.query;

import io.sillysillyman.deventer.entity.Category;
import io.sillysillyman.deventer.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryQuery {

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findAllByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);
}