package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.PasswordHistory;
import io.sillysillyman.deventer.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findByUserOrderByCreatedAtAsc(User user);
}