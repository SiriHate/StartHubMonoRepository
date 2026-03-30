package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.CommentResponseDTO;
import org.siri_hate.main_service.model.entity.article.ArticleComment;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ArticleCommentMapper {

    CommentResponseDTO toCommentResponseDTO(ArticleComment articleComment);

    List<CommentResponseDTO> toCommentResponseListDTO(List<ArticleComment> articleComments);
}
