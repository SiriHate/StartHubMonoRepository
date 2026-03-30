package org.siri_hate.user_service.service;

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
    private final String avatarsBucket;
    private final String endpoint;
    private final S3Presigner s3Presigner;
    private final Duration presignedUrlTtl;

    @Autowired
    public FileService(
            S3Client s3Client,
            @Value("${minio.avatars_bucket}") String avatarsBucket,
            @Value("${minio.endpoint}") String endpoint,
            S3Presigner s3Presigner,
            @Value("${minio.presigned-url-ttl-seconds:900}") long presignedUrlTtlSeconds
    ) {
        this.s3Client = s3Client;
        this.avatarsBucket = avatarsBucket;
        this.endpoint = trimTrailingSlash(endpoint);
        this.s3Presigner = s3Presigner;
        this.presignedUrlTtl = Duration.ofSeconds(Math.max(presignedUrlTtlSeconds, 60));
    }

    public String uploadAvatar(String username, MultipartFile file) {
        return uploadImage(username, file, avatarsBucket);
    }

    public String uploadImage(String username, MultipartFile file, String bucketName) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Avatar file must not be empty");
        }

        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.containsKey(contentType)) {
            throw new IllegalArgumentException("Unsupported avatar file type: " + contentType);
        }

        String extension = ALLOWED_TYPES.get(contentType);
        String key = buildObjectKey(username, extension);

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read avatar file", e);
        } catch (S3Exception | SdkClientException e) {
            throw new RuntimeException("Failed to upload avatar to S3", e);
        }

        return key;
    }

    public void deleteAvatar(String imageKey) {
        deleteImage(imageKey, avatarsBucket);
    }

    public void deleteImage(String imageKey, String bucketName) {

        String key = resolveObjectKey(imageKey);
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

    public String getAvatarUrl(String storedAvatarPath) {

        String key = resolveObjectKey(storedAvatarPath);
        if (key == null) {
            return null;
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(avatarsBucket)
                .key(key)
                .build();

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

    private String buildObjectKey(String username, String extension) {
        String safeUsername = (username == null ? "unknown" : username.replaceAll("[^A-Za-z0-9._-]", "_"));
        return "%s/%s%s".formatted(safeUsername, UUID.randomUUID(), extension);
    }

    private String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String resolveObjectKey(String storedAvatarPath) {
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
        if (withoutQuery.startsWith(avatarsBucket + "/")) {
            withoutQuery = withoutQuery.substring(avatarsBucket.length() + 1);
        }
        return withoutQuery;
    }
}
