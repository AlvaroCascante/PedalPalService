package com.quetoquenana.pedalpal.media.infrastructure.storage;

import com.quetoquenana.pedalpal.media.application.port.StorageProvider;
import com.quetoquenana.pedalpal.media.domain.model.SignedUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Component
@RequiredArgsConstructor
public class R2StorageProvider implements StorageProvider {

    private final S3Presigner presigner;

    @Value("${cloudflare.r2.public.bucket}")
    private String privateBucket;

    @Value("${cloudflare.r2.private.bucket}")
    private String publicBucket;

    @Value("${cloudflare.r2.presign.ttl.minutes:10}")
    private final long defaultTtlMinutes = 10;

    @Override
    public SignedUrl generateUploadUrl(String storageKey, String contentType, boolean isPublic) {
        // Build the PUT request you want the client to be allowed to perform
        String bucket = isPublic ? publicBucket : privateBucket;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(storageKey)
                .contentType(contentType) // if you set this, client must upload with same Content-Type
                .build();

        // Presign it (short TTL)
        Duration ttl = Duration.ofMinutes(defaultTtlMinutes);

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = presigner.presignPutObject(putObjectPresignRequest);

        return new SignedUrl(
                presignedPutObjectRequest.url().toString(),
                Instant.now().plus(ttl),
                Map.of(CONTENT_TYPE, contentType)
        );
    }

    @Override
    public SignedUrl generateDownloadUrl(String storageKey, String contentType, boolean isPublic) {
        String bucket = isPublic ? publicBucket : privateBucket;

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(storageKey)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(5))
                        .getObjectRequest(getObjectRequest)
                        .build();

        PresignedGetObjectRequest presignedRequest =
                presigner.presignGetObject(presignRequest);

        return new SignedUrl(
                presignedRequest.url().toString(),
                Instant.now().plus(Duration.ofMinutes(defaultTtlMinutes)),
                Map.of(CONTENT_TYPE, contentType)
        );
    }
}