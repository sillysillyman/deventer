package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Category;
import io.sillysillyman.deventer.repository.query.CategoryRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>,
    JpaSpecificationExecutor<Category>, CategoryRepositoryQuery {

}