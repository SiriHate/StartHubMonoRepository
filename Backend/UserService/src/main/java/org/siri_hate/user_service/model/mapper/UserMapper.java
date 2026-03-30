package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.CurrentUserInfoResponseDTO;
import org.siri_hate.user_service.model.entity.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    CurrentUserInfoResponseDTO userToCurrentUserInfoResponseDTO(User user);
}