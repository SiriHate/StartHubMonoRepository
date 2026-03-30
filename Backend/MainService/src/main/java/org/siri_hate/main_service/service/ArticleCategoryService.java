package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.main_service.dto.ArticleCategoryFullResponseDTO;
import org.siri_hate.main_service.dto.ArticleCategorySummaryResponseDTO;
import org.siri_hate.main_service.model.mapper.ArticleCategoryMapper;
import org.siri_hate.main_service.model.entity.article.ArticleCategory;
import org.siri_hate.main_service.repository.ArticleCategoryRepository;
import org.siri_hate.main_service.dto.ArticleCategoryRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCategoryService {

    private final ArticleCategoryRepository articleCategoryRepository;
    private final ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    public ArticleCategoryService(ArticleCategoryRepository articleCategoryRepository, ArticleCategoryMapper articleCategoryMapper) {
        this.articleCategoryRepository = articleCategoryRepository;
        this.articleCategoryMapper = articleCategoryMapper;
    }
    
    @Transactional
    public ArticleCategoryFullResponseDTO createArticleCategory(ArticleCategoryRequestDTO request) {
        ArticleCategory articleCategory = articleCategoryMapper.toArticleCategory(request);
        articleCategoryRepository.save(articleCategory);
        return articleCategoryMapper.toArticleCategoryFullResponse(articleCategory);
    }

    public List<ArticleCategorySummaryResponseDTO> getArticleCategories() {
        List<ArticleCategory> articleCategories = articleCategoryRepository.findAll();
        if (articleCategories.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return articleCategoryMapper.toArticleCategorySummaryResponseList(articleCategories);
    }
    
    public ArticleCategoryFullResponseDTO getArticleCategory(Long id) {
        ArticleCategory articleCategory = articleCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return articleCategoryMapper.toArticleCategoryFullResponse(articleCategory);
    }
    
    public ArticleCategory getArticleCategoryEntityById(Long id) {
        return articleCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public ArticleCategoryFullResponseDTO updateArticleCategory(Long id, ArticleCategoryRequestDTO request) {
        ArticleCategory articleCategory = articleCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        articleCategory = articleCategoryMapper.updateArticleCategory(request, articleCategory);
        articleCategoryRepository.save(articleCategory);
        return articleCategoryMapper.toArticleCategoryFullResponse(articleCategory);
    }

    @Transactional
    public void deleteArticleCategory(Long id) {
        ArticleCategory articleCategory = articleCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        articleCategoryRepository.delete(articleCategory);
    }
}