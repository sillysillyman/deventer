package io.sillysillyman.deventer.repository.impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sillysillyman.deventer.entity.PasswordHistory;
import io.sillysillyman.deventer.entity.QPasswordHistory;
import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.repository.query.PasswordHistoryRepositoryQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordHistoryRepositoryQueryImpl implements PasswordHistoryRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public List<PasswordHistory> findByUserOrderByCreatedAtAsc(User user) {
        QPasswordHistory passwordHistory = QPasswordHistory.passwordHistory;

        return queryFactory.selectFrom(passwordHistory).where(passwordHistory.user.eq(user))
            .orderBy(passwordHistory.createdAt.asc()).fetch();
    }
}