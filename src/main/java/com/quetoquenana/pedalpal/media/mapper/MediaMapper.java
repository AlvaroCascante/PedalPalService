package com.quetoquenana.pedalpal.media.mapper;

import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.domain.model.*;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
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
        String ext = ContentTypeExtensions.extensionFor(spec.contentType());
        String type = referenceType.name().toLowerCase(Locale.ROOT);

        return type + "/" +  //announcement, profile, etc.
                command.authenticatedUserId() + "/" +
                today.getYear() + "/" +
                String.format("%02d", today.getMonthValue()) + "/" +
                String.format("%02d", today.getDayOfMonth()) + "/" +
                mediaId + "." + ext;
    }

    public Set<UploadMediaResult> toResult(
            Set<Media> models
    ) {
        return models.stream().map(this::toResult).collect(Collectors.toSet());
    }

    private UploadMediaResult toResult(
            Media model
    ){
            return new UploadMediaResult(
                model.getId(),
                model.getSignedUrl().url(),
                model.getStorageKey(),
                model.getSignedUrl().expiresAt()
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
