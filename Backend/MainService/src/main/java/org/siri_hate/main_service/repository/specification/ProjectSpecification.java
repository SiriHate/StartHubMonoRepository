package org.siri_hate.main_service.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.ProjectCategory;
import org.siri_hate.main_service.model.entity.project.ProjectMember;
import org.siri_hate.main_service.model.entity.project.search.EmployeeSearch;
import org.siri_hate.main_service.model.entity.project.search.MentorSearch;
import org.siri_hate.main_service.model.entity.project.search.PartnerSearch;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {

    public static Specification<Project> projectNameStartsWith(String projectName) {
        return (root, query, criteriaBuilder) -> {
            if (projectName == null || projectName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("name"), projectName + "%");
        };
    }

    public static Specification<Project> projectNameContainsIgnoreCase(String projectName) {
        return (root, query, cb) -> {
            if (projectName == null || projectName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + projectName.toLowerCase() + "%");
        };
    }

    public static Specification<Project> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null || category.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Project, ProjectCategory> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("name"), category);
        };
    }

    public static Specification<Project> moderationPassed(Boolean moderationPassed) {
        if (moderationPassed == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("moderationPassed"), moderationPassed);
    }

    public static Specification<Project> hasOwnerUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("owner").get("username"), username);
        };
    }

    public static Specification<Project> hasMemberUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank()) {
                return cb.conjunction();
            }
            query.distinct(true);

            Join<Project, ProjectMember> memberJoin = root.join("members", JoinType.LEFT);
            Join<ProjectMember, User> userJoin = memberJoin.join("user", JoinType.LEFT);
            return cb.equal(userJoin.get("username"), username);
        };
    }

    public static Specification<Project> hasEmployeeSearch(String specialization) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Project, EmployeeSearch> join = root.join("employeeSearches");
            if (specialization != null && !specialization.isBlank()) {
                return cb.like(cb.lower(join.get("specialization")), "%" + specialization.toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }

    public static Specification<Project> hasFounderSearch(String domain) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Project, PartnerSearch> join = root.join("founderSearches");
            if (domain != null && !domain.isBlank()) {
                return cb.like(cb.lower(join.get("domain")), "%" + domain.toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }

    public static Specification<Project> hasInvestorSearch() {
        return (root, query, cb) -> {
            query.distinct(true);
            root.join("investorSearches");
            return cb.conjunction();
        };
    }

    public static Specification<Project> hasMentorSearch(String domain) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Project, MentorSearch> join = root.join("mentorSearches");
            if (domain != null && !domain.isBlank()) {
                return cb.like(cb.lower(join.get("domain")), "%" + domain.toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }
}