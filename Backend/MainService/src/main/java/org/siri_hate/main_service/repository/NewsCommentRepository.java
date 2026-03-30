package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.news.NewsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsCommentRepository extends JpaRepository<NewsComment, Long> {
    List<NewsComment> findByNewsId(Long newsId);
}
