package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.SeekingRoleDTO;
import org.siri_hate.main_service.model.entity.project.search.SeekingRole;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SearchMapper {
    SeekingRole toSeekingRole(SeekingRoleDTO dto);
}
