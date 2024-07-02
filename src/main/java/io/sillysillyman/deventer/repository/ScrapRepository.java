package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.Scrap;
import io.sillysillyman.deventer.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findByUserAndPost(User user, Post post);

    List<Scrap> findAllByUser(User user);
}
