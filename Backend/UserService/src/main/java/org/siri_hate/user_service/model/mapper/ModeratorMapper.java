package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.ModeratorFullResponseDTO;
import org.siri_hate.user_service.dto.ModeratorPageResponseDTO;
import org.siri_hate.user_service.dto.ModeratorRequestDTO;
import org.siri_hate.user_service.dto.ModeratorSummaryResponseDTO;
import org.siri_hate.user_service.model.entity.Moderator;
import org.siri_hate.user_service.model.mapper.resolver.CredentialResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
          CredentialResolver.class
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ModeratorMapper {

    @Mapping(target = "password", source = "password", qualifiedByName = "toEncryptedPassword")
    Moderator toModerator(ModeratorRequestDTO request);

    ModeratorFullResponseDTO toModeratorFullResponseDTO(Moderator moderator);

    List<ModeratorFullResponseDTO> toModeratorsFullResponseDTO(List<Moderator> moderators);

    ModeratorSummaryResponseDTO toModeratorSummaryResponseDTO(Moderator moderator);

    List<ModeratorSummaryResponseDTO> toModeratorsSummaryResponseDTO(List<Moderator> moderators);

    Moderator updateModerator(ModeratorRequestDTO moderatorFullRequest, @MappingTarget Moderator moderator);

    ModeratorPageResponseDTO toModeratorPageResponseDTO(Page<Moderator> page);
}