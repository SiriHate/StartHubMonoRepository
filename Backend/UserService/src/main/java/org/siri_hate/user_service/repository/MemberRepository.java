package org.siri_hate.user_service.repository;

import org.siri_hate.user_service.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findMemberByUsername(String username);

    Optional<Member> findMemberByEmail(String email);
}