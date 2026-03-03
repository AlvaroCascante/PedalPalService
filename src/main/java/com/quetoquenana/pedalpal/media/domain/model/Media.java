package com.quetoquenana.pedalpal.media.domain.model;

import com.quetoquenana.pedalpal.common.exception.BusinessException;
import lombok.*;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    private UUID id;
    private UUID ownerId;
    private UUID referenceId;
    private MediaReferenceType referenceType;
    private MediaType mediaType;
    private String contentType;
    private Boolean isPrimary;
    private MediaStatus status;
    private String storageKey;
    private String provider;
    private String providerAssetId;
    private Long sizeBytes;
    private Map<String, Object> metadata;
    private String title;
    private String altText;
    private SignedUrl signedUrl;

    public void confirmUploaded(
            String providerAssetId,
            Long sizeBytes,
            Map<String, Object> metadata
    ) {
        if (this.status != MediaStatus.PENDING) {
            throw new BusinessException("media.not.pending");
        }
        this.providerAssetId = providerAssetId;
        this.sizeBytes = sizeBytes;
        if (metadata != null && !metadata.isEmpty()) {
            this.metadata.putAll(metadata);
        }
        this.status = MediaStatus.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Media media)) return false;
        return Objects.equals(id, media.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}