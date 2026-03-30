package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.CommentRequestDTO;
import org.siri_hate.main_service.dto.CommentResponseDTO;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.news.News;
import org.siri_hate.main_service.model.entity.news.NewsComment;
import org.siri_hate.main_service.model.mapper.NewsCommentMapper;
import org.siri_hate.main_service.repository.NewsCommentRepository;
import org.siri_hate.main_service.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewsCommentService {

    private final NewsCommentRepository newsCommentRepository;
    private final NewsRepository newsRepository;
    private final UserService userService;
    private final NewsCommentMapper newsCommentMapper;

    @Autowired
    public NewsCommentService(
            NewsCommentRepository newsCommentRepository,
            NewsRepository newsRepository,
            UserService userService,
            NewsCommentMapper newsCommentMapper
    ) {
        this.newsCommentRepository = newsCommentRepository;
        this.newsRepository = newsRepository;
        this.userService = userService;
        this.newsCommentMapper = newsCommentMapper;
    }

    @Transactional
    public CommentResponseDTO createComment(String username, Long newsId, CommentRequestDTO request) {
        News news = newsRepository.findById(newsId).orElseThrow(EntityNotFoundException::new);
        User user = userService.findOrCreateUser(username);
        NewsComment comment = new NewsComment(user, news, request.getText());
        newsCommentRepository.save(comment);
        return newsCommentMapper.toCommentResponseDTO(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        NewsComment comment = newsCommentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);
        String commentOwnerUsername = comment.getAuthor().getUsername();
        if (!commentOwnerUsername.equals(username)) {
            throw new RuntimeException("Only the author can delete the comment");
        }
        newsCommentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getNewsComments(Long newsId) {
        List<NewsComment> comments = newsCommentRepository.findByNewsId(newsId);
        return newsCommentMapper.toCommentResponseListDTO(comments);
    }
}
