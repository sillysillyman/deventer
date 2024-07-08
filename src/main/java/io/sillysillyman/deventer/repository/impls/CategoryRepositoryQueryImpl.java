package io.sillysillyman.deventer.repository.impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sillysillyman.deventer.entity.Category;
import io.sillysillyman.deventer.entity.QCategory;
import io.sillysillyman.deventer.repository.query.CategoryRepositoryQuery;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryRepositoryQueryImpl implements CategoryRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByTopic(String topic) {
        QCategory category = QCategory.category;

        return queryFactory.selectOne().from(category).where(category.topic.eq(topic)).fetchFirst()
            != null;
    }

    @Override
    public Optional<Category> findByTopic(String categoryTopic) {
        QCategory category = QCategory.category;

        return Optional.ofNullable(
            queryFactory.selectFrom(category).where(category.topic.eq(categoryTopic)).fetchOne());
    }
}