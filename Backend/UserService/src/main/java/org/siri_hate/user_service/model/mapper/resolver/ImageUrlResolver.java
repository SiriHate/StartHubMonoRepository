package org.siri_hate.user_service.model.mapper.resolver;

import org.mapstruct.Named;
import org.siri_hate.user_service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageUrlResolver {

    private final FileService fileService;

    @Autowired
    public ImageUrlResolver(FileService fileService) {
        this.fileService = fileService;
    }

    @Named("toAvatarUrl")
    public String toUrl(String avatarKey) {
        return avatarKey == null ? null : fileService.getAvatarUrl(avatarKey);
    }
}
