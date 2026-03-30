package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.ProjectFullResponseDTO;
import org.siri_hate.main_service.dto.ProjectPageResponseDTO;
import org.siri_hate.main_service.dto.ProjectRequestDTO;
import org.siri_hate.main_service.dto.ProjectSummaryResponseDTO;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.mapper.utils.CategoryResolver;
import org.siri_hate.main_service.model.mapper.utils.ImageUrlResolver;
import org.siri_hate.main_service.model.mapper.utils.UserResolver;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                ProjectMemberMapper.class,
                CommentMapper.class,
                ImageUrlResolver.class,
                CategoryResolver.class,
                UserResolver.class
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProjectMapper {

    @Mapping(target = "category", ignore = true)
    Project toProject(ProjectRequestDTO project);

    void projectUpdate(ProjectRequestDTO request, @MappingTarget Project project);

    @Mapping(target = "logoUrl", source = "imageKey", qualifiedByName = "toProjectUrl")
    @Mapping(target = "category", source = "category", qualifiedByName = "toProjectCategoryName")
    @Mapping(target = "hasSurvey", expression = "java(project.getSurvey() != null)")
    ProjectFullResponseDTO toProjectFullResponse(Project project);

    @Mapping(target = "logoUrl", source = "imageKey", qualifiedByName = "toProjectUrl")
    @Mapping(target = "category", source = "category", qualifiedByName = "toProjectCategoryName")
    @Mapping(target = "owner", source = "owner", qualifiedByName = "toUsername")
    ProjectSummaryResponseDTO toProjectSummaryResponse(Project project);

    ProjectPageResponseDTO toProjectPageResponse(Page<Project> projects);
}