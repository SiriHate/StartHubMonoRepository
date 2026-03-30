package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.MentorSearchFullResponseDTO;
import org.siri_hate.main_service.dto.MentorSearchRequestDTO;
import org.siri_hate.main_service.model.entity.project.search.MentorSearch;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MentorSearchMapper {
    MentorSearch toEntity(MentorSearchRequestDTO request);

    void updateEntity(MentorSearchRequestDTO request, @MappingTarget MentorSearch entity);

    MentorSearchFullResponseDTO toFullResponse(MentorSearch entity);

    List<MentorSearchFullResponseDTO> toFullResponseList(List<MentorSearch> entities);
}
