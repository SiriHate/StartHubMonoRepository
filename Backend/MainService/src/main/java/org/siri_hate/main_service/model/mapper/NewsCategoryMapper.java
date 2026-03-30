package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.NewsCategoryFullResponseDTO;
import org.siri_hate.main_service.dto.NewsCategoryRequestDTO;
import org.siri_hate.main_service.dto.NewsCategorySummaryResponseDTO;
import org.siri_hate.main_service.model.entity.news.NewsCategory;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NewsCategoryMapper {
    NewsCategory toNewsCategory(NewsCategoryRequestDTO request);

    NewsCategoryFullResponseDTO toNewsCategoryFullResponse(NewsCategory newsCategory);

    List<NewsCategorySummaryResponseDTO> toNewsCategorySummaryResponseList(List<NewsCategory> newsCategories);

    NewsCategory updateNewsCategory(NewsCategoryRequestDTO request, @MappingTarget NewsCategory newsCategory);
}