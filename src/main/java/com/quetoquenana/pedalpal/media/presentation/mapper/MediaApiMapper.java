package com.quetoquenana.pedalpal.media.presentation.mapper;

import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.presentation.dto.request.ConfirmUploadRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaSpecRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.response.UploadMediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MediaApiMapper {

    public UploadMediaCommand toCommand(
            UploadMediaRequest request,
            UUID authenticatedUserId,
            boolean isAdmin
    ) {
        return new UploadMediaCommand(
                authenticatedUserId,
                isAdmin,
                request.isPublic(),
                request.referenceId(),
                MediaReferenceType.from(request.referenceType()),
                request.mediaSpecs().stream().map(this::toCommand).collect(Collectors.toSet())
        );
    }

    private UploadMediaSpecCommand toCommand(UploadMediaSpecRequest request) {
        return new UploadMediaSpecCommand(
                request.mediaType(),
                request.contentType(),
                request.isPrimary(),
                request.name(),
                request.altText()
        );
    }

    public UploadMediaResponse toResponse(UploadMediaResult result) {
        return new UploadMediaResponse(
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
