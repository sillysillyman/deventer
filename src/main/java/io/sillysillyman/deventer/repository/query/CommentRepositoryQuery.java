package io.sillysillyman.deventer.repository.query;

import io.sillysillyman.deventer.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryQuery {

    List<Comment> findAllByPostId(long postId);

    Page<Comment> findByUserId(Long userId, Pageable pageable);
}