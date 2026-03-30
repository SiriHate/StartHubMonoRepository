package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.CommentResponseDTO;
import org.siri_hate.main_service.model.entity.news.NewsComment;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NewsCommentMapper {

    CommentResponseDTO toCommentResponseDTO(NewsComment newsComment);

    List<CommentResponseDTO> toCommentResponseListDTO(List<NewsComment> newsComments);
}
