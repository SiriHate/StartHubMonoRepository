package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.project.ProjectSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSubscriberRepository extends JpaRepository<ProjectSubscriber, Long> {
    List<ProjectSubscriber> findByProjectId(Long projectId);

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    void deleteByProjectIdAndUserId(Long projectId, Long userId);
} 