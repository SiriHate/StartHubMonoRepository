package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.ArticleCategoryFullResponseDTO;
import org.siri_hate.main_service.dto.ArticleCategoryRequestDTO;
import org.siri_hate.main_service.dto.ArticleCategorySummaryResponseDTO;
import org.siri_hate.main_service.model.entity.article.ArticleCategory;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ArticleCategoryMapper {
    ArticleCategory toArticleCategory(ArticleCategoryRequestDTO articleCategoryRequest);

    ArticleCategoryFullResponseDTO toArticleCategoryFullResponse(ArticleCategory articleCategory);

    List<ArticleCategorySummaryResponseDTO> toArticleCategorySummaryResponseList(List<ArticleCategory> articleCategories);

    ArticleCategory updateArticleCategory(ArticleCategoryRequestDTO request, @MappingTarget ArticleCategory articleCategory);
}