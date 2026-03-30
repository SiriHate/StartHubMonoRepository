package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.ProfileTypeDTO;
import org.siri_hate.user_service.model.entity.member_profiles.MemberProfileType;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MemberProfileMapper {
    MemberProfileType toMemberProfileType(ProfileTypeDTO profileType);
}
