package com.quetoquenana.pedalpal.media.mapper;

import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.command.GenerateUploadUrlCommand;
import com.quetoquenana.pedalpal.media.application.result.GenerateUploadUrlResult;
import com.quetoquenana.pedalpal.media.presentation.dto.request.ConfirmUploadRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.response.GenerateUploadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MediaApiMapper {

    public GenerateUploadUrlCommand toCommand(
            UploadMediaRequest request,
            UUID authenticatedUserId,
            String provider,
            boolean isAdmin
    ) {
        return new GenerateUploadUrlCommand(
                request.ownerId(),
                authenticatedUserId,
                isAdmin,
                request.referenceId(),
                request.referenceType(),
                request.isPrimary(),
                request.mediaType(),
                request.contentType(),
                request.title(),
                request.altText(),
                provider
        );
    }

    public GenerateUploadUrlResponse toResponse(GenerateUploadUrlResult result) {
        return new GenerateUploadUrlResponse(
                result.mediaId(),
                result.uploadUrl(),
                result.storageKey(),
                result.expiresAt()
        );
    }

    public ConfirmUploadCommand toCommand(
            ConfirmUploadRequest request,
            UUID authenticatedUserId
    ) {
        return new ConfirmUploadCommand(
                request.storageKey(),
                request.providerAssetId(),
                authenticatedUserId,
                request.sizeBytes(),
                request.metadata()
        );
    }
}
