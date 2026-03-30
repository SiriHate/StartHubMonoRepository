package org.siri_hate.user_service.repository;

import org.siri_hate.user_service.model.entity.member_profiles.JobSeekerSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSeekerSpecializationRepository extends JpaRepository<JobSeekerSpecialization, Long> {
}