package io.sillysillyman.deventer.repository.impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.entity.like.CommentLike;
import io.sillysillyman.deventer.entity.like.QCommentLike;
import io.sillysillyman.deventer.repository.query.CommentLikeRepositoryQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentLikeRepositoryQueryImpl implements CommentLikeRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countByCommentId(Long commentId) {
        QCommentLike commentLike = QCommentLike.commentLike;

        return queryFactory.selectFrom(commentLike).where(commentLike.comment.id.eq(commentId))
            .fetch().size();
    }

    @Override
    public Optional<CommentLike> findByCommentAndUser(Comment comment, User user) {
        QCommentLike commentLike = QCommentLike.commentLike;

        return Optional.ofNullable(queryFactory.selectFrom(commentLike)
            .where(commentLike.comment.eq(comment).and(commentLike.user.eq(user))).fetchOne());
    }

    @Override
    public Page<CommentLike> findByUserOrderByCreatedAtDesc(User user, Pageable pageable) {
        QCommentLike commentLike = QCommentLike.commentLike;

        List<CommentLike> commentLikes = queryFactory.selectFrom(commentLike)
            .where(commentLike.user.eq(user)).orderBy(commentLike.createdAt.desc())
            .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(commentLike).where(commentLike.user.eq(user)).fetch()
            .size();

        return new PageImpl<>(commentLikes, pageable, total);
    }
}