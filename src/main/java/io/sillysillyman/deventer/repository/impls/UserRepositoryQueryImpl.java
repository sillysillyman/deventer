package io.sillysillyman.deventer.repository.impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sillysillyman.deventer.entity.QUser;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.enums.UserLoginType;
import io.sillysillyman.deventer.repository.query.UserRepositoryQuery;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public boolean existsByUsername(String username) {
        QUser user = QUser.user;

        return queryFactory.selectOne().from(user).where(user.username.eq(username)).fetchFirst()
            != null;
    }

    public boolean existsByNickname(String nickname) {
        QUser user = QUser.user;

        return queryFactory.selectOne().from(user).where(user.nickname.eq(nickname)).fetchFirst()
            != null;
    }

    public boolean existsByEmail(String email) {
        QUser user = QUser.user;

        return queryFactory.selectOne().from(user).where(user.email.eq(email)).fetchFirst() != null;
    }

    public Optional<User> findByUsername(String username) {
        QUser user = QUser.user;

        return Optional.ofNullable(
            queryFactory.selectFrom(user).where(user.username.eq(username)).fetchOne());
    }

    public Optional<User> findByEmailAndLoginType(String email, UserLoginType loginType) {
        QUser user = QUser.user;

        return Optional.ofNullable(queryFactory.selectFrom(user)
            .where(user.email.eq(email).and(user.loginType.eq(loginType))).fetchOne());
    }
}
