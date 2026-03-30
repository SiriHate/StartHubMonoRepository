package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.project.search.InvestorSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorSearchRepository extends JpaRepository<InvestorSearch, Long> {
    List<InvestorSearch> findByProjectId(Long projectId);

    boolean existsByProjectId(Long projectId);
}
