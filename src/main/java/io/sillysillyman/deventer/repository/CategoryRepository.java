package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByTopic(String categoryTopic);

    boolean existsByTopic(String topic);
}
