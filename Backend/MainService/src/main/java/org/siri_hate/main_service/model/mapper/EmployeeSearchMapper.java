package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.EmployeeSearchFullResponseDTO;
import org.siri_hate.main_service.dto.EmployeeSearchRequestDTO;
import org.siri_hate.main_service.model.entity.project.search.EmployeeSearch;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EmployeeSearchMapper {
    EmployeeSearch toEntity(EmployeeSearchRequestDTO request);

    void updateEntity(EmployeeSearchRequestDTO request, @MappingTarget EmployeeSearch entity);

    EmployeeSearchFullResponseDTO toFullResponse(EmployeeSearch entity);

    List<EmployeeSearchFullResponseDTO> toFullResponseList(List<EmployeeSearch> entities);
}