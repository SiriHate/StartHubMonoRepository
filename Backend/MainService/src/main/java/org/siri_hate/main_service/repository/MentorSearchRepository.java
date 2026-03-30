package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.project.search.MentorSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorSearchRepository extends JpaRepository<MentorSearch, Long> {
    List<MentorSearch> findByProjectId(Long projectId);
}
