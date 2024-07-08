package io.sillysillyman.deventer.repository.impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sillysillyman.deventer.entity.Category;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.QPost;
import io.sillysillyman.deventer.repository.query.PostRepositoryQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findByUserId(Long userId, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> posts = queryFactory.selectFrom(post).where(post.user.id.eq(userId))
            .orderBy(post.createdAt.desc()).offset(pageable.getOffset())
            .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(post).where(post.user.id.eq(userId)).fetch().size();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        QPost post = QPost.post;

        List<Post> posts = queryFactory.selectFrom(post).orderBy(post.createdAt.desc())
            .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(post).fetch().size();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findAllByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> posts = queryFactory.selectFrom(post).where(post.category.eq(category))
            .orderBy(post.createdAt.desc()).offset(pageable.getOffset())
            .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(post).where(post.category.eq(category)).fetch().size();

        return new PageImpl<>(posts, pageable, total);
    }
}