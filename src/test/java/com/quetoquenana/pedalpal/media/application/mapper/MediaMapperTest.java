package com.quetoquenana.pedalpal.media.application.mapper;

import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaContentType;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for MediaMapper update behavior.
 */
public class MediaMapperTest {

    /**
     * Ensures the update mapper preserves the existing version for JPA merge.
     */
    @Test
    public void toUpdatedModel_preservesVersionAndId() {
        MediaMapper mapper = new MediaMapper();
        UUID referenceId = UUID.randomUUID();
        UUID mediaId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        long version = 3L;

        UploadMediaSpecCommand spec = new UploadMediaSpecCommand(
                "image/jpeg",
                "bike-photo",
                "Bike photo"
        );
        UploadMediaCommand command = new UploadMediaCommand(
                true,
                referenceId,
                MediaReferenceType.BIKE_PROFILE,
                List.of(spec)
        );

        Media existing = Media.builder()
                .id(mediaId)
                .referenceId(referenceId)
                .referenceType(MediaReferenceType.BIKE_PROFILE)
                .contentType(MediaContentType.IMAGE_JPEG)
                .status(MediaStatus.ACTIVE)
                .storageKey("old/key")
                .provider("r2")
                .build();
        existing.setVersion(version);

        Media updated = mapper.toUpdatedModel(existing, ownerId, command, spec, "r2");

        assertEquals(mediaId, updated.getId());
        assertEquals(version, updated.getVersion());
    }
}

