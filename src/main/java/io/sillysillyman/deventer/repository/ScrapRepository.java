package io.sillysillyman.deventer.repository;

import io.sillysillyman.deventer.entity.Scrap;
import io.sillysillyman.deventer.repository.query.ScrapRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScrapRepository extends JpaRepository<Scrap, Long>,
    JpaSpecificationExecutor<Scrap>,
    ScrapRepositoryQuery {

}