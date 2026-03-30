package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.AdminFullResponseDTO;
import org.siri_hate.user_service.dto.AdminPageResponseDTO;
import org.siri_hate.user_service.dto.AdminRequestDTO;
import org.siri_hate.user_service.dto.AdminSummaryResponseDTO;
import org.siri_hate.user_service.model.entity.Admin;
import org.siri_hate.user_service.model.mapper.resolver.CredentialResolver;
import org.springframework.data.domain.Page;


@Mapper(
        componentModel = "spring",
        uses = {
                CredentialResolver.class
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AdminMapper {

    @Mapping(target = "password", source = "password", qualifiedByName = "toEncryptedPassword")
    Admin toAdmin(AdminRequestDTO request);

    AdminFullResponseDTO toAdminFullResponseDTO(Admin admin);

    AdminSummaryResponseDTO toAdminSummaryResponseDTO(Admin admin);

    Admin adminUpdate(AdminRequestDTO request, @MappingTarget Admin admin);

    AdminPageResponseDTO toAdminPageResponseDTO(Page<Admin> page);
}