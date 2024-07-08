package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.PasswordHistory;
import io.sillysillyman.deventer.repository.query.PasswordHistoryRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long>,
    JpaSpecificationExecutor<PasswordHistory>, PasswordHistoryRepositoryQuery {

}