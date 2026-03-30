package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.JobSeekerProfileRequestDTO;
import org.siri_hate.user_service.dto.JobSeekerProfileResponseDTO;
import org.siri_hate.user_service.model.entity.member_profiles.JobSeekerProfile;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface JobSeekerProfileMapper {

    @Mapping(target = "specialization", ignore = true)
    JobSeekerProfile toJobSeekerProfile(JobSeekerProfileRequestDTO request);

    @Mapping(source = "specialization.name", target = "specialization")
    JobSeekerProfileResponseDTO toJobSeekerProfileResponseDTO(JobSeekerProfile jobSeekerProfile);

    @Mapping(target = "memberId", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    void updateJobSeekerProfile(JobSeekerProfileRequestDTO request, @MappingTarget JobSeekerProfile profile);
}
