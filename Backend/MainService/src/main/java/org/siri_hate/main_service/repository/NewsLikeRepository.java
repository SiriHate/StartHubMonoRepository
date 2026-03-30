package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.news.NewsLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsLikeRepository extends JpaRepository<NewsLike, Long> {
    Optional<NewsLike> findByNewsIdAndUserId(Long newsId, Long userId);
    long countByNewsId(Long newsId);
}
