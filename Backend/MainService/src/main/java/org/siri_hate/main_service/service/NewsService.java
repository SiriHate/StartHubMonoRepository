package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.main_service.dto.NewsFullResponseDTO;
import org.siri_hate.main_service.dto.NewsPageResponseDTO;
import org.siri_hate.main_service.dto.NewsRequestDTO;
import org.siri_hate.main_service.model.mapper.NewsMapper;
import org.siri_hate.main_service.model.entity.news.News;
import org.siri_hate.main_service.repository.NewsRepository;
import org.siri_hate.main_service.repository.specification.NewsSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final NewsCategoryService newsCategoryService;
    private final UserService userService;
    private final FileService fileService;

    @Autowired
    public NewsService(
            NewsRepository newsRepository,
            NewsMapper newsMapper,
            NewsCategoryService newsCategoryService,
            UserService userService,
            FileService fileService
    )
    {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.newsCategoryService = newsCategoryService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @Transactional
    public NewsFullResponseDTO createNews(String username, NewsRequestDTO request, MultipartFile logo) {
        News news = newsMapper.toNews(request);
        news.setAuthor(userService.findOrCreateUser(username));
        news.setCategory(newsCategoryService.getNewsCategoryEntityById(request.getCategoryId()));
        String imageKey = fileService.uploadNewsLogo(logo);
        news.setImageKey(imageKey);
        newsRepository.save(news);
        return newsMapper.toNewsFullResponse(news);
    }

    public NewsFullResponseDTO getNewsById(Long id) {
        News news = newsRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return newsMapper.toNewsFullResponse(news);
    }

    public NewsPageResponseDTO getNews(String category, String query, int page, int size, boolean isModerationPassed) {
        Specification<News> specification = Specification.allOf(
                NewsSpecification.titleStartsWith(query),
                NewsSpecification.hasCategory(category),
                NewsSpecification.moderationPassed(isModerationPassed)
        );
        Page<News> news = newsRepository.findAll(specification, PageRequest.of(page, size));
        if (news.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return newsMapper.toNewsPageResponse(news);
    }

    public NewsPageResponseDTO getNews(String username, String query, int page, int size) {
        Specification<News> spec = Specification.allOf(
                NewsSpecification.hasUserUsername(username),
                NewsSpecification.titleStartsWith(query)
        );
        Page<News> news = newsRepository.findAll(spec, PageRequest.of(page, size));
        if (news.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return newsMapper.toNewsPageResponse(news);
    }

    @Transactional
    public NewsFullResponseDTO updateNews(Long id, NewsRequestDTO request, MultipartFile logo) {
        News news = newsRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        news = newsMapper.newsUpdate(request, news);
        if (logo != null && !logo.isEmpty()) {
            String imageKey = fileService.uploadNewsLogo(logo);
            news.setImageKey(imageKey);
        }
        newsRepository.save(news);
        return newsMapper.toNewsFullResponse(news);
    }

    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        newsRepository.delete(news);
    }

    @Transactional
    public void updateNewsModerationStatus(Long id, Boolean moderationPassed) {
        News news = newsRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        news.setModerationPassed(moderationPassed);
        newsRepository.save(news);
    }
}