package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.news.News;
import org.siri_hate.main_service.model.entity.news.NewsLike;
import org.siri_hate.main_service.repository.NewsLikeRepository;
import org.siri_hate.main_service.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class NewsLikeService {

    private final NewsLikeRepository newsLikeRepository;
    private final NewsRepository newsRepository;
    private final UserService userService;

    @Autowired
    public NewsLikeService(
            NewsLikeRepository newsLikeRepository,
            NewsRepository newsRepository,
            UserService userService
    ) {
        this.newsLikeRepository = newsLikeRepository;
        this.newsRepository = newsRepository;
        this.userService = userService;
    }

    @Transactional
    public boolean toggleLike(String username, Long newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(EntityNotFoundException::new);
        User user = userService.findOrCreateUser(username);
        Optional<NewsLike> existingLike = newsLikeRepository.findByNewsIdAndUserId(newsId, user.getId());
        if (existingLike.isPresent()) {
            newsLikeRepository.delete(existingLike.get());
            return false;
        } else {
            newsLikeRepository.save(new NewsLike(user, news));
            return true;
        }
    }

    @Transactional(readOnly = true)
    public long getLikesCount(Long newsId) {
        return newsLikeRepository.countByNewsId(newsId);
    }
}
