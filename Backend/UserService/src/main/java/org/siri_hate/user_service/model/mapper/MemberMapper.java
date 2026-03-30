package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.*;
import org.siri_hate.user_service.model.entity.Member;
import org.siri_hate.user_service.model.mapper.resolver.CredentialResolver;
import org.siri_hate.user_service.model.mapper.resolver.ImageUrlResolver;
import org.siri_hate.user_service.model.yandex.YandexUserInfo;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring",
        uses = {
                ImageUrlResolver.class,
                CredentialResolver.class,
                FounderProfileMapper.class,
                InvestorProfileMapper.class,
                MentorProfileSeeker.class,
                JobSeekerProfileMapper.class
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MemberMapper {

    @Mapping(target = "password", source = "password", qualifiedByName = "toEncryptedPassword")
    @Mapping(target = "authType", constant = "PASSWORD")
    Member toMember(MemberRegistrationRequestDTO request);

    @Mapping(target = "authType", constant = "YANDEX")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "login")
    @Mapping(target = "phone", source = "defaultPhone.number")
    @Mapping(target = "email", source = "defaultEmail")
    @Mapping(target = "name", source = "realName")
    Member toMember(YandexUserInfo userInfo);

    @Mapping(target = "avatarUrl", source = "avatarKey", qualifiedByName = "toAvatarUrl")
    MemberFullResponseDTO toMemberFullResponseDTO(Member member);

    @Mapping(target = "avatarUrl", source = "avatarKey", qualifiedByName = "toAvatarUrl")
    MemberSummaryResponseDTO toMemberSummaryResponseDTO(Member member);

    Member updateMember(MemberProfileDataRequestDTO request, @MappingTarget Member member);

    MemberPageResponseDTO toMemberPageResponseDTO(Page<Member> page);
}
