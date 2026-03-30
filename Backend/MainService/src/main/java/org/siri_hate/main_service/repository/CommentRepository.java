package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.project.ProjectComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<ProjectComment, Long> {
    List<ProjectComment> findByProjectId(Long projectId);
} 