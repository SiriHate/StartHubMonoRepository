package org.siri_hate.user_service.repository.adapters;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.siri_hate.user_service.model.entity.Member;
import org.siri_hate.user_service.model.entity.member_profiles.*;
import org.springframework.data.jpa.domain.Specification;

public final class MemberSpecification {

    public static Specification<Member> usernameContainsIgnoreCase(String query) {
        return (root, cq, cb) -> {
            if (query == null || query.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("username")), "%" + query.toLowerCase() + "%");
        };
    }

    public static Specification<Member> hasJobSeekerProfile() {
        return (root, cq, cb) -> cb.isNotNull(root.get("jobSeekerProfile"));
    }

    public static Specification<Member> hasMentorProfile() {
        return (root, cq, cb) -> cb.isNotNull(root.get("mentorProfile"));
    }

    public static Specification<Member> hasInvestorProfile() {
        return (root, cq, cb) -> cb.isNotNull(root.get("investorProfile"));
    }

    public static Specification<Member> hasFounderProfile() {
        return (root, cq, cb) -> cb.isNotNull(root.get("founderProfile"));
    }

    public static Specification<Member> jobSeekerHasSpecialization(String specialization) {
        return (root, cq, cb) -> {
            if (specialization == null || specialization.isEmpty()) return cb.conjunction();
            Join<Member, JobSeekerProfile> profile = root.join("jobSeekerProfile", JoinType.INNER);
            cq.distinct(true);
            return cb.equal(cb.lower(profile.get("specialization").get("name")), specialization.toLowerCase());
        };
    }

    public static Specification<Member> investorHasDomain(String domain) {
        return (root, cq, cb) -> {
            if (domain == null || domain.isEmpty()) return cb.conjunction();
            Join<Member, InvestorProfile> profile = root.join("investorProfile", JoinType.INNER);
            Join<InvestorProfile, Domain> domains = profile.join("domains", JoinType.INNER);
            cq.distinct(true);
            return cb.equal(cb.lower(domains.get("name")), domain.toLowerCase());
        };
    }

    public static Specification<Member> mentorHasDomain(String domain) {
        return (root, cq, cb) -> {
            if (domain == null || domain.isEmpty()) return cb.conjunction();
            Join<Member, MentorProfile> profile = root.join("mentorProfile", JoinType.INNER);
            Join<MentorProfile, Domain> domains = profile.join("domains", JoinType.INNER);
            cq.distinct(true);
            return cb.equal(cb.lower(domains.get("name")), domain.toLowerCase());
        };
    }

    public static Specification<Member> founderHasDomain(String domain) {
        return (root, cq, cb) -> {
            if (domain == null || domain.isEmpty()) return cb.conjunction();
            Join<Member, FounderProfile> profile = root.join("founderProfile", JoinType.INNER);
            Join<FounderProfile, Domain> domains = profile.join("domains", JoinType.INNER);
            cq.distinct(true);
            return cb.equal(cb.lower(domains.get("name")), domain.toLowerCase());
        };
    }
}
