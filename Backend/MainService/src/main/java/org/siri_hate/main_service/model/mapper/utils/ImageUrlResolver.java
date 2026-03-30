package org.siri_hate.main_service.model.mapper.utils;

import org.mapstruct.Named;
import org.siri_hate.main_service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ImageUrlResolver {

    private final FileService fileService;

    @Autowired
    public ImageUrlResolver(FileService fileService) {
        this.fileService = fileService;
    }

    @Named("toArticleUrl")
    public URI toArticleUrl(String avatarKey) {
        return avatarKey == null ? null : URI.create(fileService.getArticleLogoUrl(avatarKey));
    }

    @Named("toNewsUrl")
    public URI toNewsUrl(String avatarKey) {
        return avatarKey == null ? null : URI.create(fileService.getNewsLogoUrl(avatarKey));
    }

    @Named("toProjectUrl")
    public URI toProjectUrl(String avatarKey) {
        return avatarKey == null ? null : URI.create(fileService.getProjectLogoUrl(avatarKey));
    }
}
