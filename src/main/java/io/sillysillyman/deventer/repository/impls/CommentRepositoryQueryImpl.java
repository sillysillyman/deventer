package io.sillysillyman.deventer.repository.impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sillysillyman.deventer.entity.Comment;
import io.sillysillyman.deventer.entity.QComment;
import io.sillysillyman.deventer.repository.query.CommentRepositoryQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentRepositoryQueryImpl implements CommentRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public List<Comment> findAllByPostId(long postId) {
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment).where(comment.post.id.eq(postId)).fetch();
    }

    public Page<Comment> findByUserId(Long userId, Pageable pageable) {
        QComment comment = QComment.comment;

        List<Comment> comments = queryFactory.selectFrom(comment).where(comment.user.id.eq(userId))
            .orderBy(comment.createdAt.desc()).offset(pageable.getOffset())
            .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(comment).where(comment.user.id.eq(userId)).fetch()
            .size();

        return new PageImpl<>(comments, pageable, total);
    }
}