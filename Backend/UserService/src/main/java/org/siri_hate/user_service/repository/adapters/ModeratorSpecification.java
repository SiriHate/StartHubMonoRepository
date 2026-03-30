package org.siri_hate.user_service.repository.adapters;

import org.siri_hate.user_service.model.entity.Moderator;
import org.springframework.data.jpa.domain.Specification;

public class ModeratorSpecification {

    public static Specification<Moderator> usernameStartsWithIgnoreCase(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"
            );
        };
    }
}
