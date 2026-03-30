package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.InvestorSearchFullResponseDTO;
import org.siri_hate.main_service.dto.InvestorSearchRequestDTO;
import org.siri_hate.main_service.model.entity.project.search.InvestorSearch;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface InvestorSearchMapper {
    InvestorSearch toEntity(InvestorSearchRequestDTO request);

    void updateEntity(InvestorSearchRequestDTO request, @MappingTarget InvestorSearch entity);

    InvestorSearchFullResponseDTO toFullResponse(InvestorSearch entity);

    List<InvestorSearchFullResponseDTO> toFullResponseList(List<InvestorSearch> entities);
}
