package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.FounderProfileRequestDTO;
import org.siri_hate.user_service.dto.FounderProfileResponseDTO;
import org.siri_hate.user_service.model.entity.member_profiles.Domain;
import org.siri_hate.user_service.model.entity.member_profiles.FounderProfile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FounderProfileMapper {

    @Mapping(target = "domains", ignore = true)
    FounderProfile toFounderProfile(FounderProfileRequestDTO request);

    @Mapping(source = "domains", target = "domains", qualifiedByName = "domainsToNames")
    FounderProfileResponseDTO toFounderProfileResponseDTO(FounderProfile profile);

    @Mapping(target = "memberId", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "domains", ignore = true)
    void updateFounderProfile(FounderProfileRequestDTO request, @MappingTarget FounderProfile profile);

    @Named("domainsToNames")
    default List<String> domainsToNames(Set<Domain> domains) {
        if (domains == null) return List.of();
        return domains.stream().map(Domain::getName).collect(Collectors.toList());
    }
}
