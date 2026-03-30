package org.siri_hate.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.user_service.dto.JobSeekerSpecializationResponseDTO;
import org.siri_hate.user_service.model.entity.member_profiles.JobSeekerSpecialization;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface JobSeekerSpecializationMapper {
    JobSeekerSpecializationResponseDTO toJobSeekerSpecializationResponse(JobSeekerSpecialization jobSeekerSpecialization);

    List<JobSeekerSpecializationResponseDTO> toJobSeekerSpecializationListResponse(List<JobSeekerSpecialization> jobSeekerSpecializations);
}