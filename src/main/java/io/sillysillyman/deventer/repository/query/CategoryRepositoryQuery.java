package io.sillysillyman.deventer.repository.query;

import io.sillysillyman.deventer.entity.Category;
import java.util.Optional;

public interface CategoryRepositoryQuery {

    boolean existsByTopic(String topic);

    Optional<Category> findByTopic(String categoryTopic);
}