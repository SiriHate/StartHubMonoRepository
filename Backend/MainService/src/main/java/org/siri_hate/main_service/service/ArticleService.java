package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.main_service.dto.ArticleFullResponseDTO;
import org.siri_hate.main_service.dto.ArticlePageResponseDTO;
import org.siri_hate.main_service.dto.ArticleRequestDTO;
import org.siri_hate.main_service.model.mapper.ArticleMapper;
import org.siri_hate.main_service.model.entity.article.Article;
import org.siri_hate.main_service.repository.ArticleRepository;
import org.siri_hate.main_service.repository.specification.ArticleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCategoryService articleCategoryService;
    private final ArticleMapper articleMapper;
    private final UserService userService;
    private final FileService fileService;

    @Autowired
    public ArticleService(
            ArticleRepository articleRepository,
            ArticleMapper articleMapper,
            ArticleCategoryService articleCategoryService,
            UserService userService,
            FileService fileService
    )
    {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.articleCategoryService = articleCategoryService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @Transactional
    public ArticleFullResponseDTO createArticle(String username, ArticleRequestDTO request, MultipartFile logo) {
        Article article = articleMapper.toArticle(request);
        article.setAuthor(userService.findOrCreateUser(username));
        article.setCategory(articleCategoryService.getArticleCategoryEntityById(request.getCategoryId()));
        String imageKey = fileService.uploadArticleLogo(logo);
        article.setImageKey(imageKey);
        articleRepository.save(article);
        return articleMapper.toArticleFullResponseDTO(article);
    }

    @Transactional
    public ArticleFullResponseDTO getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return articleMapper.toArticleFullResponseDTO(article);
    }

    public ArticlePageResponseDTO getArticles(String category, String query, int page, int size, boolean isModerationPassed) {
        Specification<Article> spec = Specification.allOf(
                ArticleSpecification.titleStartsWith(query),
                ArticleSpecification.hasCategory(category),
                ArticleSpecification.moderationPassed(isModerationPassed)
        );
        Page<Article> articles = articleRepository.findAll(spec, PageRequest.of(page, size));
        if (articles.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return articleMapper.toArticlePageResponseDTO(articles);
    }

    public ArticlePageResponseDTO getArticles(String username, String query, int page, int size) {
        Specification<Article> specification = Specification.allOf(
                ArticleSpecification.hasUserUsername(username),
                ArticleSpecification.titleStartsWith(query)
        );
        Page<Article> articles = articleRepository.findAll(specification, PageRequest.of(page, size));
        if (articles.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return articleMapper.toArticlePageResponseDTO(articles);
    }

    @Transactional
    public ArticleFullResponseDTO updateArticle(Long id, ArticleRequestDTO request, MultipartFile logo) {
        Article article = articleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        article = articleMapper.articleUpdate(request, article);
        if (logo != null && !logo.isEmpty()) {
            String imageKey = fileService.uploadArticleLogo(logo);
            article.setImageKey(imageKey);
        }
        articleRepository.save(article);
        return articleMapper.toArticleFullResponseDTO(article);
    }

    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        articleRepository.delete(article);
    }

    @Transactional
    public void updateArticleModerationStatus(Long id, Boolean isModerationPassed) {
        Article article = articleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        article.setModerationPassed(isModerationPassed);
        articleRepository.save(article);
    }
}