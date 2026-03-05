package com.quetoquenana.pedalpal.media.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MediaTest {

    @Test
    void confirmUploaded_shouldActivateWithoutMetadata_whenMetadataIsNull() {
        Media media = Media.builder()
                .id(UUID.randomUUID())
                .status(MediaStatus.DRAFT)
                .build();

        Media confirmed = media.confirmUploaded("asset-1", 123L, null);

        assertEquals(MediaStatus.ACTIVE, confirmed.getStatus());
        assertEquals("asset-1", confirmed.getProviderAssetId());
        assertEquals(123L, confirmed.getSizeBytes());
        assertNull(confirmed.getMetadata());
    }

    @Test
    void confirmUploaded_shouldInitializeMetadata_whenMetadataProvided() {
        Media media = Media.builder()
                .id(UUID.randomUUID())
                .status(MediaStatus.DRAFT)
                .build();

        Media confirmed = media.confirmUploaded("asset-1", 123L, Map.of("width", 800));

        assertEquals(MediaStatus.ACTIVE, confirmed.getStatus());
        assertNotNull(confirmed.getMetadata());
        assertEquals(800, confirmed.getMetadata().get("width"));
    }
}
