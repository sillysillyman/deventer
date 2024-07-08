package io.sillysillyman.deventer.repository.impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sillysillyman.deventer.entity.Post;
import io.sillysillyman.deventer.entity.QScrap;
import io.sillysillyman.deventer.entity.Scrap;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.repository.query.ScrapRepositoryQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ScrapRepositoryQueryImpl implements ScrapRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public Optional<Scrap> findByUserAndPost(User user, Post post) {
        QScrap scrap = QScrap.scrap;

        return Optional.ofNullable(
            queryFactory.selectFrom(scrap).where(scrap.user.eq(user).and(scrap.post.eq(post)))
                .fetchOne());
    }

    public Page<Scrap> findAllByUserOrderByPostCreatedAtDesc(User user, Pageable pageable) {
        QScrap scrap = QScrap.scrap;

        List<Scrap> scraps = queryFactory.selectFrom(scrap).where(scrap.user.eq(user))
            .orderBy(scrap.post.createdAt.desc()).offset(pageable.getOffset())
            .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(scrap).where(scrap.user.eq(user)).fetch().size();

        return new PageImpl<>(scraps, pageable, total);
    }

    public Page<Scrap> findAllByUserOrderByPostUserNicknameAsc(User user, Pageable pageable) {
        QScrap scrap = QScrap.scrap;

        List<Scrap> scraps = queryFactory.selectFrom(scrap).where(scrap.user.eq(user))
            .orderBy(scrap.post.user.nickname.asc()).offset(pageable.getOffset())
            .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.selectFrom(scrap).where(scrap.user.eq(user)).fetch().size();

        return new PageImpl<>(scraps, pageable, total);
    }
}