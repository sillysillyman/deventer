package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.repository.query.UserRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>,
    UserRepositoryQuery {

}