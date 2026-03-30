package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.project.search.EmployeeSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeSearchRepository extends JpaRepository<EmployeeSearch, Long> {
    List<EmployeeSearch> findByProjectId(Long projectId);
}
