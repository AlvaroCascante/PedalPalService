package com.quetoquenana.pedalpal.media.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MediaTest {

    @Test
    void confirmUploaded_shouldActivateWithoutMetadata_whenMetadataIsNull() {
        Media media = Media.builder()
                .id(UUID.randomUUID())
                .status(MediaStatus.DRAFT)
                .build();

        media.confirmUploaded();

        assertEquals(MediaStatus.ACTIVE, media.getStatus());
    }
}
