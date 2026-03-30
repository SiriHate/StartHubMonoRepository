package org.siri_hate.user_service.model.mapper.resolver;

import org.mapstruct.Named;
import org.siri_hate.user_service.model.entity.member_profiles.JobSeekerSpecialization;
import org.siri_hate.user_service.service.MemberProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecializationResolver {

    private final MemberProfileService memberProfileService;

    @Autowired
    public SpecializationResolver(MemberProfileService memberProfileService) {
        this.memberProfileService = memberProfileService;
    }

    @Named("toMemberSpecialization")
    public JobSeekerSpecialization toJobSeekerSpecialization(Long specializationId) {
        return specializationId == null ? null : memberProfileService.getJobSeekerSpecializationEntity(specializationId);
    }
}