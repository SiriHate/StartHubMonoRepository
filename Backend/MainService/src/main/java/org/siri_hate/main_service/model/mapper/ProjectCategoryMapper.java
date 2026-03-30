package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.ProjectCategoryFullResponseDTO;
import org.siri_hate.main_service.dto.ProjectCategoryRequestDTO;
import org.siri_hate.main_service.dto.ProjectCategorySummaryResponseDTO;
import org.siri_hate.main_service.model.entity.project.ProjectCategory;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProjectCategoryMapper {
    ProjectCategory toProjectCategory(ProjectCategoryRequestDTO projectCategoryRequest);

    ProjectCategoryFullResponseDTO toProjectCategoryFullResponse(ProjectCategory projectCategory);

    List<ProjectCategorySummaryResponseDTO> toProjectCategoriesSummaryResponse(List<ProjectCategory> projectCategories);

    ProjectCategory updateProjectCategoryFromRequest(ProjectCategoryRequestDTO request, @MappingTarget ProjectCategory projectCategory);
}