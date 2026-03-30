package org.siri_hate.main_service.model.mapper.utils;

import org.mapstruct.Named;
import org.siri_hate.main_service.model.entity.article.ArticleCategory;
import org.siri_hate.main_service.model.entity.news.NewsCategory;
import org.siri_hate.main_service.model.entity.project.ProjectCategory;
import org.siri_hate.main_service.service.ArticleCategoryService;
import org.siri_hate.main_service.service.NewsCategoryService;
import org.siri_hate.main_service.service.ProjectCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryResolver {

    @Named("toArticleCategoryName")
    public String toArticleCategory(ArticleCategory articleCategory) {
        return articleCategory == null ? null :articleCategory.getName();
    }

    @Named("toNewsCategoryName")
    public String toNewsCategory(NewsCategory newsCategory) {
        return newsCategory == null ? null : newsCategory.getName();
    }

    @Named("toProjectCategoryName")
    public String toProjectCategory(ProjectCategory projectCategory) {
        return projectCategory == null ? null : projectCategory.getName();
    }

}
