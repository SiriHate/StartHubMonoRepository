package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.news.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {

}