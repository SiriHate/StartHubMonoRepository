package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.InvestorProfileRequestDTO;
import org.siri_hate.user_service.dto.InvestorProfileResponseDTO;
import org.siri_hate.user_service.model.entity.member_profiles.Domain;
import org.siri_hate.user_service.model.entity.member_profiles.InvestorProfile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface InvestorProfileMapper {

    @Mapping(target = "domains", ignore = true)
    InvestorProfile toInvestorProfile(InvestorProfileRequestDTO request);

    @Mapping(source = "domains", target = "domains", qualifiedByName = "domainsToNames")
    InvestorProfileResponseDTO toInvestorProfileResponseDTO(InvestorProfile investorProfile);

    @Mapping(target = "memberId", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "domains", ignore = true)
    void updateInvestorProfile(InvestorProfileRequestDTO request, @MappingTarget InvestorProfile profile);

    @Named("domainsToNames")
    default List<String> domainsToNames(Set<Domain> domains) {
        if (domains == null) return List.of();
        return domains.stream().map(Domain::getName).collect(Collectors.toList());
    }
}
