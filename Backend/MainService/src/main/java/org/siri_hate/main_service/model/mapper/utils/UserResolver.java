package org.siri_hate.main_service.model.mapper.utils;

import org.mapstruct.Named;
import org.siri_hate.main_service.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserResolver {

    @Named("toUsername")
    public String toUsername(User user) {
        return user == null ? null : user.getUsername();
    }
}
