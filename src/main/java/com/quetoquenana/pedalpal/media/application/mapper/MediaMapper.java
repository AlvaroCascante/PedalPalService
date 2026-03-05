package com.quetoquenana.pedalpal.media.application.mapper;

import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.media.application.model.SignedUrl;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.domain.model.*;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MediaMapper {

    public Media toModel(UploadMediaCommand command,
                         UploadMediaSpecCommand spec
    ) {
        UUID mediaId = UUID.randomUUID();
        return Media.builder()
                .id(mediaId)
                .ownerId(command.authenticatedUserId())
                .referenceId(command.referenceId())
                .referenceType(command.referenceType())
                .mediaType(MediaType.from(spec.mediaType()))
                .contentType(spec.contentType())
                .isPrimary(spec.isPrimary())
                .status(MediaStatus.DRAFT)
                .storageKey(buildStorageKey(
                        mediaId,
                        command,
                        spec,
                        command.referenceType(),
                        Clock.systemDefaultZone())
                )
                // provider may be decided by backend/storageProvider; keep unset unless command/spec provides it
                .title(spec.name())
                .altText(spec.altText())
                .build();
    }

    private String buildStorageKey(
            UUID mediaId,
            UploadMediaCommand command,
            UploadMediaSpecCommand spec,
            MediaReferenceType referenceType,
            Clock clock
    ) {
        LocalDate today = LocalDate.now(clock);
        String ext = MediaContentType.extensionFor(spec.contentType());
        String type = referenceType.name().toLowerCase(Locale.ROOT);

        return type + "/" +  //announcement, profile, etc.
                command.authenticatedUserId() + "/" +
                today.getYear() + "/" +
                String.format("%02d", today.getMonthValue()) + "/" +
                String.format("%02d", today.getDayOfMonth()) + "/" +
                mediaId + "." + ext;
    }

    public Set<UploadMediaResult> toResult(
            Set<Media> models,
            java.util.Map<UUID, SignedUrl> signedUrls
    ) {
        return models.stream()
                .map(model -> toResult(model, signedUrls.get(model.getId())))
                .collect(Collectors.toSet());
    }

    public UploadMediaResult toResult(
            Media model,
            SignedUrl signedUrl
    ) {
        return new UploadMediaResult(
                model.getId(),
                signedUrl.url(),
                model.getStorageKey(),
                signedUrl.expiresAt()
        );
    }

    public ConfirmedUploadResult toConfirmedResult(Media model, String cdnUrl) {
        return new ConfirmedUploadResult(
            model.getId(),
            model.getStorageKey(),
            model.getStatus().name(),
            cdnUrl
        );
    }
}
