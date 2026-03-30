package org.siri_hate.user_service.repository;

import org.siri_hate.user_service.model.entity.member_profiles.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
}
