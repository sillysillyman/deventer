package io.sillysillyman.deventer.repository.query;

import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.Scrap;
import io.sillysillyman.deventer.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScrapRepositoryQuery {

    Optional<Scrap> findByUserAndPost(User user, Post post);

    Page<Scrap> findAllByUserOrderByPostCreatedAtDesc(User user, Pageable pageable);

    Page<Scrap> findAllByUserOrderByPostUserNicknameAsc(User user, Pageable pageable);
}