package com.quetoquenana.pedalpal.media.application.mapper;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.media.application.model.SignedUrl;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaContentType;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

public class MediaMapper {

    public Media toModel(UUID ownerId,
                         UploadMediaCommand command,
                         UploadMediaSpecCommand spec
    ) {
        UUID mediaId = UUID.randomUUID();
        return Media.builder()
                .id(mediaId)
                .referenceId(command.referenceId())
                .referenceType(command.referenceType())
                .contentType(MediaContentType.fromContentType(spec.contentType()))
                .isPrimary(spec.isPrimary())
                .status(MediaStatus.DRAFT)
                .storageKey(buildStorageKey(
                        mediaId,
                        ownerId,
                        command,
                        spec,
                        command.referenceType(),
                        Clock.systemDefaultZone())
                )
                .name(spec.name())
                .altText(spec.altText())
                .build();
    }

    private String buildStorageKey(
            UUID mediaId,
            UUID ownerId,
            UploadMediaCommand command,
            UploadMediaSpecCommand spec,
            MediaReferenceType referenceType,
            Clock clock
    ) {
        LocalDate today = LocalDate.now(clock);
        String ext = MediaContentType.extensionFor(spec.contentType());
        String type = referenceType.name().toLowerCase(Locale.ROOT);

        return type + "/" +  //announcement, profile, etc.
                ownerId + "/" +
                today.getYear() + "/" +
                String.format("%02d", today.getMonthValue()) + "/" +
                String.format("%02d", today.getDayOfMonth()) + "/" +
                mediaId + "." + ext;
    }

    public MediaResult toResult(Media model, SignedUrl signedUrl) {
        return this.toResult(model, signedUrl.url(), signedUrl.expiresAt());
    }

    public MediaResult toResult(Media model, String cdnUrl) {
        return this.toResult(model, cdnUrl, null);
    }

    public MediaResult toResult(Media model, String cdnUrl, Instant expiration) {

        return new MediaResult(
                model.getId(),
                model.getContentType().name(),
                model.getProvider(),
                model.getIsPrimary(),
                model.getStatus(),
                model.getName(),
                model.getAltText(),
                cdnUrl,
                expiration
        );
    }
}
