package com.quetoquenana.pedalpal.media.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing media associated to a reference.
 */
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Media extends Auditable {

    private UUID id;
    private UUID ownerId; // ID of the user who owns this media (could be the uploader or the entity owner, depending on the use case)
    private UUID referenceId;   // ID of the entity this media is associated with (e.g., announcement, user profile, bike, etc.)
    private MediaReferenceType referenceType; // e.g., ANNOUNCEMENT, USER_PROFILE, BIKE, etc.
    private MediaType mediaType; // e.g., IMAGE, VIDEO, DOCUMENT, etc.
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

    /**
     * Confirms an upload by storing provider details and activating the media.
     */
    public void confirmUploaded() {
        this.status = MediaStatus.ACTIVE;
    }

    /**
     * Assigns the storage provider for the media.
     */
    public Media assignProvider(String provider) {
        return this.toBuilder()
                .provider(provider)
                .build();
    }

    private Map<String, Object> mergeMetadata(Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return this.metadata;
        }
        Map<String, Object> merged = new HashMap<>();
        if (this.metadata != null && !this.metadata.isEmpty()) {
            merged.putAll(this.metadata);
        }
        merged.putAll(metadata);
        return merged;
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