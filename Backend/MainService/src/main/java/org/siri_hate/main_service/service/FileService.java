package org.siri_hate.main_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
public class FileService {

    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );
    private final S3Client s3Client;
    private final String articlesLogoBucket;
    private final String newsLogoBucket;
    private final String projectsLogoBucket;
    private final String endpoint;
    private final S3Presigner s3Presigner;
    private final Duration presignedUrlTtl;

    @Autowired
    public FileService(
            S3Client s3Client,
            @Value("${minio.articles_logo_bucket}") String articlesLogoBucket,
            @Value("${minio.news_logo_bucket}") String newsLogoBucket,
            @Value("${minio.projects_logo_bucket}") String projectsLogoBucket,
            @Value("${minio.endpoint}") String endpoint,
            S3Presigner s3Presigner,
            @Value("${minio.presigned-url-ttl-seconds:900}") long presignedUrlTtlSeconds
    ) {
        this.s3Client = s3Client;
        this.articlesLogoBucket = articlesLogoBucket;
        this.newsLogoBucket = newsLogoBucket;
        this.projectsLogoBucket = projectsLogoBucket;
        this.endpoint = trimTrailingSlash(endpoint);
        this.s3Presigner = s3Presigner;
        this.presignedUrlTtl = Duration.ofSeconds(Math.max(presignedUrlTtlSeconds, 60));
    }

    public String uploadArticleLogo(MultipartFile file) {
        return uploadImage(file, articlesLogoBucket);
    }

    public String uploadNewsLogo(MultipartFile file) {
        return uploadImage(file, newsLogoBucket);
    }

    public String uploadProjectLogo(MultipartFile file) {
        return uploadImage(file, projectsLogoBucket);
    }

    public void deleteArticleImage(String imageKey) {
        deleteImage(imageKey, articlesLogoBucket);
    }

    public void deleteNewsImage(String imageKey) {
        deleteImage(imageKey, newsLogoBucket);
    }

    public void deleteProject(String imageKey) {
        deleteImage(imageKey, projectsLogoBucket);
    }

    public String getArticleLogoUrl(String imageKey) {
        return getImageUrl(imageKey, articlesLogoBucket);
    }

    public String getNewsLogoUrl(String imageKey) {
        return getImageUrl(imageKey, newsLogoBucket);
    }

    public String getProjectLogoUrl(String imageKey) {
        return getImageUrl(imageKey, projectsLogoBucket);
    }

    private String uploadImage(MultipartFile file, String bucketName) {
        if (file == null || file.isEmpty()) {throw new IllegalArgumentException("Avatar file must not be empty");}
        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.containsKey(contentType)) {
            throw new IllegalArgumentException("Unsupported avatar file type: %s".formatted(contentType));
        }
        String extension = ALLOWED_TYPES.get(contentType);
        String key = buildObjectKey(extension);
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key)
                    .contentType(contentType).build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read avatar file", e);
        } catch (S3Exception | SdkClientException e) {
            throw new RuntimeException("Failed to upload avatar to S3", e);
        }
        return key;
    }

    private void deleteImage(String imageKey, String bucketName) {

        String key = resolveObjectKey(imageKey, bucketName);
        if (key == null) {
            return;
        }

        try {
            s3Client.deleteObject(builder -> builder
                    .bucket(bucketName)
                    .key(key)
            );
        } catch (S3Exception | SdkClientException e) {
            throw new RuntimeException("Failed to delete avatar from S3", e);
        }
    }

    private String getImageUrl(String storedAvatarPath, String bucketName) {

        String key = resolveObjectKey(storedAvatarPath, bucketName);
        if (key == null) {
            return null;
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        try {
            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .getObjectRequest(getObjectRequest)
                            .signatureDuration(presignedUrlTtl)
                            .build()
            );
            return presignedGetObjectRequest.url().toString();
        } catch (S3Exception | SdkClientException e) {
            throw new RuntimeException("Failed to generate presigned URL for avatar", e);
        }
    }

    private String buildObjectKey(String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString();
        return timestamp + "-" + uuid + extension;
    }

    private String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String resolveObjectKey(String storedAvatarPath, String bucketName) {
        if (storedAvatarPath == null || storedAvatarPath.isBlank()) {
            return null;
        }
        String withoutQuery = storedAvatarPath.split("\\?", 2)[0];
        String normalizedEndpoint = endpoint;
        if (!normalizedEndpoint.isEmpty()) {
            normalizedEndpoint = normalizedEndpoint + "/";
            if (withoutQuery.startsWith(normalizedEndpoint)) {
                withoutQuery = withoutQuery.substring(normalizedEndpoint.length());
            }
        }
        if (withoutQuery.startsWith(bucketName + "/")) {
            withoutQuery = withoutQuery.substring(bucketName.length() + 1);
        }
        return withoutQuery;
    }
}
