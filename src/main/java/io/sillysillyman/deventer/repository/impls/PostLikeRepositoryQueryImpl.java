package io.sillysillyman.deventer.repository.impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.entity.like.PostLike;
import io.sillysillyman.deventer.entity.like.QPostLike;
import io.sillysillyman.deventer.repository.query.PostLikeRepositoryQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostLikeRepositoryQueryImpl implements PostLikeRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public long countByPostId(Long postId) {
        QPostLike postLike = QPostLike.postLike;

        return queryFactory.selectFrom(postLike).where(postLike.post.id.eq(postId)).fetch().size();
    }

    public Optional<PostLike> findByPostAndUser(Post post, User user) {
        QPostLike postLike = QPostLike.postLike;

        return Optional.ofNullable(queryFactory.selectFrom(postLike)
            .where(postLike.post.eq(post).and(postLike.user.eq(user))).fetchOne());
    }

    public Page<PostLike> findByUserOrderByCreatedAtDesc(User user, Pageable pageable) {
        QPostLike postLike = QPostLike.postLike;

        List<PostLike> postLikes = queryFactory.selectFrom(postLike).where(postLike.user.eq(user))
            .orderBy(postLike.createdAt.desc()).offset(pageable.getOffset())
            .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(postLike).where(postLike.user.eq(user)).fetch().size();

        return new PageImpl<>(postLikes, pageable, total);
    }
}