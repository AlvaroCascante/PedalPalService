package com.quetoquenana.pedalpal.media.mapper;

import com.quetoquenana.pedalpal.media.application.command.GenerateUploadUrlCommand;
import com.quetoquenana.pedalpal.media.application.result.ConfirmedUploadResult;
import com.quetoquenana.pedalpal.media.application.result.GenerateUploadUrlResult;
import com.quetoquenana.pedalpal.media.domain.model.*;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Component
public class MediaMapper {

    public Media toModel(GenerateUploadUrlCommand command) {
        UUID mediaId = UUID.randomUUID();
        MediaReferenceType referenceType = MediaReferenceType.from(command.referenceType());
        return Media.builder()
                .id(mediaId)
                .ownerId(command.ownerId())
                .referenceId(command.referenceId())
                .referenceType(referenceType)
                .mediaType(MediaType.from(command.mediaType()))
                .contentType(command.contentType())
                .isPrimary(command.isPrimary())
                .status(MediaStatus.PENDING)
                .storageKey(buildStorageKey(
                        mediaId,
                        command,
                        referenceType,
                        Clock.systemDefaultZone())
                )
                .provider(command.provider())
                .title(command.title())
                .altText(command.altText())
                .build();
    }

    private String buildStorageKey(
            UUID mediaId,
            GenerateUploadUrlCommand command,
            MediaReferenceType referenceType,
            Clock clock
    ) {
        LocalDate today = LocalDate.now(clock);
        String ext = ContentTypeExtensions.extensionFor(command.contentType());
        String type = referenceType.name().toLowerCase(Locale.ROOT);

        return type + "/" +  //announcement, profile, etc.
                command.ownerId() + "/" +
                today.getYear() + "/" +
                String.format("%02d", today.getMonthValue()) + "/" +
                String.format("%02d", today.getDayOfMonth()) + "/" +
                mediaId + "." + ext;
    }

    public GenerateUploadUrlResult toResult(
            Media model,
            SignedUrl signedUrl
    ){
        return new GenerateUploadUrlResult(
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
