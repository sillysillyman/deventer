package io.sillysillyman.deventer.repository.query;

import io.sillysillyman.deventer.entity.PasswordHistory;
import io.sillysillyman.deventer.entity.User;
import java.util.List;

public interface PasswordHistoryRepositoryQuery {

    List<PasswordHistory> findByUserOrderByCreatedAtAsc(User user);
}