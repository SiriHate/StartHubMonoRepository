package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.MentorProfileRequestDTO;
import org.siri_hate.user_service.dto.MentorProfileResponseDTO;
import org.siri_hate.user_service.model.entity.member_profiles.Domain;
import org.siri_hate.user_service.model.entity.member_profiles.MentorProfile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MentorProfileSeeker {

    @Mapping(target = "domains", ignore = true)
    MentorProfile toMentorProfile(MentorProfileRequestDTO request);

    @Mapping(source = "domains", target = "domains", qualifiedByName = "domainsToNames")
    MentorProfileResponseDTO toMentorProfileResponseDTO(MentorProfile mentorProfile);

    @Mapping(target = "memberId", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "domains", ignore = true)
    void updateMentorProfile(MentorProfileRequestDTO request, @MappingTarget MentorProfile profile);

    @Named("domainsToNames")
    default List<String> domainsToNames(Set<Domain> domains) {
        if (domains == null) return List.of();
        return domains.stream().map(Domain::getName).collect(Collectors.toList());
    }
}
