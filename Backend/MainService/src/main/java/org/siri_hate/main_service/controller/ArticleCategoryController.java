package org.siri_hate.main_service.controller;

import org.siri_hate.main_service.api.ArticleCategoryApi;
import org.siri_hate.main_service.dto.ArticleCategoryFullResponseDTO;
import org.siri_hate.main_service.dto.ArticleCategoryRequestDTO;
import org.siri_hate.main_service.dto.ArticleCategorySummaryResponseDTO;
import org.siri_hate.main_service.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleCategoryController implements ArticleCategoryApi {

    private final ArticleCategoryService articleCategoryService;

    @Autowired
    public ArticleCategoryController(ArticleCategoryService articleCategoryService) {
        this.articleCategoryService = articleCategoryService;
    }

    @Override
    public ResponseEntity<ArticleCategoryFullResponseDTO> createArticleCategory(ArticleCategoryRequestDTO articleCategoryRequestDTO) {
        ArticleCategoryFullResponseDTO response = articleCategoryService.createArticleCategory(articleCategoryRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteArticleCategory(Long id) {
        articleCategoryService.deleteArticleCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ArticleCategorySummaryResponseDTO>> getArticleCategories() {
        List<ArticleCategorySummaryResponseDTO> articleCategories = articleCategoryService.getArticleCategories();
        return new ResponseEntity<>(articleCategories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArticleCategoryFullResponseDTO> getArticleCategory(Long id) {
        ArticleCategoryFullResponseDTO response = articleCategoryService.getArticleCategory(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArticleCategoryFullResponseDTO> updateArticleCategory(Long id, ArticleCategoryRequestDTO articleCategoryRequestDTO) {
        ArticleCategoryFullResponseDTO response = articleCategoryService.updateArticleCategory(id, articleCategoryRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}