package com.quetoquenana.pedalpal.media.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private UUID referenceId;   // ID of the entity this media is associated with (e.g., announcement, user profile, bike, etc.)
    private MediaReferenceType referenceType; // e.g., ANNOUNCEMENT, USER_PROFILE, BIKE, etc.
    private MediaContentType contentType;
    private String provider;
    private Boolean isPrimary;
    private MediaStatus status;
    private String storageKey;
    private String name;
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