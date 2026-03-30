package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.CommentRequestDTO;
import org.siri_hate.main_service.dto.CommentResponseDTO;
import org.siri_hate.main_service.model.entity.project.ProjectComment;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommentMapper {
    ProjectComment commentDtoToComment(CommentRequestDTO commentRequest);

    CommentResponseDTO toCommentResponseDTO(ProjectComment projectComment);

    List<CommentResponseDTO> toCommentFullResponseListDTO(List<ProjectComment> projectComments);
}