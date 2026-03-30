package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.ProjectMemberFullResponseDTO;
import org.siri_hate.main_service.model.entity.project.ProjectMember;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProjectMemberMapper {

    @Mapping(source = "id", target = "memberId")
    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "role", target = "role")
    ProjectMemberFullResponseDTO toProjectMembersFullResponseDTO(ProjectMember projectMember);

    List<ProjectMemberFullResponseDTO> toProjectMembersFullResponseDTOList(List<ProjectMember> members);
}